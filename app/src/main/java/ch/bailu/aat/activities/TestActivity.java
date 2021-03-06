package ch.bailu.aat.activities;


import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

import java.io.File;

import ch.bailu.aat.R;
import ch.bailu.aat.description.AccelerationDescription;
import ch.bailu.aat.description.AccuracyDescription;
import ch.bailu.aat.description.AltitudeDescription;
import ch.bailu.aat.description.AverageSpeedDescription;
import ch.bailu.aat.description.BearingDescription;
import ch.bailu.aat.description.CH1903EastingDescription;
import ch.bailu.aat.description.CH1903NorthingDescription;
import ch.bailu.aat.description.CaloriesDescription;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.description.CurrentSpeedDescription;
import ch.bailu.aat.description.DateDescription;
import ch.bailu.aat.description.EndDateDescription;
import ch.bailu.aat.description.GpsStateDescription;
import ch.bailu.aat.description.LatitudeDescription;
import ch.bailu.aat.description.LongitudeDescription;
import ch.bailu.aat.description.MaximumSpeedDescription;
import ch.bailu.aat.description.NameDescription;
import ch.bailu.aat.description.PathDescription;
import ch.bailu.aat.description.PauseDescription;
import ch.bailu.aat.description.TimeDescription;
import ch.bailu.aat.description.TrackSizeDescription;
import ch.bailu.aat.description.TrackerStateDescription;
import ch.bailu.aat.dispatcher.CurrentLocationSource;
import ch.bailu.aat.dispatcher.OverlaySource;
import ch.bailu.aat.dispatcher.TrackerSource;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.map.MapViewInterface;
import ch.bailu.aat.map.layer.CurrentLocationLayer;
import ch.bailu.aat.map.layer.Dem3NameLayer;
import ch.bailu.aat.map.layer.ZoomLevel;
import ch.bailu.aat.map.layer.control.InformationBarLayer;
import ch.bailu.aat.map.layer.control.NavigationBarLayer;
import ch.bailu.aat.map.layer.gpx.GpxDynLayer;
import ch.bailu.aat.map.layer.gpx.GpxOverlayListLayer;
import ch.bailu.aat.map.layer.gpx.GpxTestLayer;
import ch.bailu.aat.map.layer.grid.GridDynLayer;
import ch.bailu.aat.map.mapsforge.MapsForgeView;
import ch.bailu.aat.test.PreferencesFromSdcard;
import ch.bailu.aat.test.PreferencesToSdcard;
import ch.bailu.aat.test.TestCoordinates;
import ch.bailu.aat.test.TestGpx;
import ch.bailu.aat.test.TestGpxLogRecovery;
import ch.bailu.aat.test.TestTest;
import ch.bailu.aat.test.UnitTest;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.aat.views.AbsLabelTextView;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.MainControlBar;
import ch.bailu.aat.views.StatusTextView;
import ch.bailu.aat.views.description.MultiView;
import ch.bailu.aat.views.preferences.VerticalScrollView;

public class TestActivity extends AbsDispatcher {
    private static final String SOLID_KEY = "test";

    private StatusTextView statusTextView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final LinearLayout contentView = new ContentView(this);

        MultiView multiView = createMultiView();

        contentView.addView(createButtonBar(multiView));
        contentView.addView(multiView);


        setContentView(contentView);

        createDispatcher();

    }


    private MultiView createMultiView() {
        ContentDescription locationDescription[] = new ContentDescription[]{
                new NameDescription(this),
                new GpsStateDescription(this),
                new AltitudeDescription(this),
                new LongitudeDescription(this),
                new LatitudeDescription(this),
                new CH1903EastingDescription(this),
                new CH1903NorthingDescription(this),
                new AccuracyDescription(this),
                new CurrentSpeedDescription(this),
                new AccelerationDescription(this),
                new BearingDescription(this),
        };

        ContentDescription trackerDescription[] = new ContentDescription[]{
                new NameDescription(this),
                new PathDescription(this),
                new TrackerStateDescription(this),
                new AverageSpeedDescription(this),
                new MaximumSpeedDescription(this),
                new CaloriesDescription(this),
                new DateDescription(this),
                new EndDateDescription(this),
                new TimeDescription(this),
                new PauseDescription(this),
                new TrackSizeDescription(this),
        };


        VerticalScrollView locationView = new VerticalScrollView(this);
        locationView.addAllContent(this, locationDescription, InfoID.LOCATION);

        VerticalScrollView trackerView = new VerticalScrollView(this);
        trackerView.addAllContent(this, trackerDescription, InfoID.TRACKER);

        VerticalScrollView testsView = new VerticalScrollView(this);

        testsView.add(new TestEntryView(new TestCoordinates(this)));
        testsView.add(new TestEntryView(new TestGpx(this)));
        testsView.add(new TestEntryView(new TestGpxLogRecovery(this)));
        testsView.add(new TestEntryView(new TestTest(this)));
        testsView.add(new TestEntryView(new PreferencesToSdcard(this)));
        testsView.add(new TestEntryView(new PreferencesFromSdcard(this)));

        VerticalScrollView speedView = new VerticalScrollView(this);

        speedView.add(this, new CurrentSpeedDescription(this), InfoID.LOCATION);
        speedView.add(this, new CurrentSpeedDescription(this), InfoID.TRACKER);


        statusTextView = new StatusTextView(this);



        final MultiView mv = new MultiView(this, SOLID_KEY);


//        ////////////////////////////////////////////////////////////
//        MapView mapView = new MapView(this);
//
//        mapView.setClickable(true);
//        mapView.getMapScaleBar().setVisible(true);
//        mapView.setBuiltInZoomControls(true);
//        mapView.setZoomLevelMin((byte) 10);
//        mapView.setZoomLevelMax((byte) 20);
//
//        // create a tile cache of suitable size
//        TileCache tileCache = AndroidUtil.createTileCache(this, "mapcache",
//                mapView.getModel().displayModel.getTileSize(), 1f,
//                mapView.getModel().frameBufferModel.getOverdrawFactor());
//
//        // tile renderer layer using internal render theme
//        MapDataStore mapDataStore = new MapFile(new File("/storage/C973-F26F/maps/berlin.map"));
//        TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore,
//                mapView.getModel().mapViewPosition, AndroidGraphicFactory.INSTANCE);
//        tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.DEFAULT);
//
//        // only once a layer is associated with a mapView the rendering starts
//        mapView.getLayerManager().getLayers().add(tileRendererLayer);
//
//        mapView.setCenter(new LatLong(52.517037, 13.38886));
//        mapView.setZoomLevel((byte) 12);

//        mv.add(mapView);
        /////////////////////////////////////////////////////////////
//        final MapsForgeView mf = new MapsForgeView(getServiceContext(), this, SOLID_KEY);

//        fillMap(mf);

//        mv.add(mf, "MapsForge");
        mv.add(locationView, getString(R.string.gps));

        mv.add(trackerView, getString(R.string.tracker));
        mv.add(speedView, "GPS vs Tracker*");

        mv.add(testsView, "Tests*");
        mv.add(statusTextView, getString(R.string.intro_status));

        return mv;
    }


    private void fillMap(MapViewInterface m) {
        m.add(new NavigationBarLayer(m.getMContext(), this));
        m.add(new ZoomLevel());
        m.add(new GridDynLayer(m.getMContext()));
        m.add(new InformationBarLayer(m.getMContext(), this));
        m.add(new CurrentLocationLayer(m.getMContext(), this));
        m.add(new Dem3NameLayer());
        m.add(new GpxTestLayer(m.getMContext(), this, InfoID.OVERLAY));
        m.add(new GpxDynLayer(m.getMContext(), this, InfoID.TRACKER));
        m.add(new GpxOverlayListLayer(m.getMContext(), this));
    }


    private ControlBar createButtonBar(MultiView multiView) {
        final MainControlBar bar = new MainControlBar(this);

        bar.addAll(multiView);
        return bar;
    }



    private void createDispatcher() {
        addSource(new TrackerSource(getServiceContext()));
        addSource(new CurrentLocationSource(getServiceContext()));
        addSource(new OverlaySource(getServiceContext()));
    }

    @Override
    public void onResumeWithService() {
        super.onResumeWithService();
        statusTextView.updateText(this);
    }


    private class TestEntryView extends AbsLabelTextView {

        public TestEntryView(final UnitTest test) {
            super(test.getContext(), test.getClass().getSimpleName());

            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        test.test();
                        setText("Test successfull");
                        AppLog.i(getContext(), "Test sucessfull");

                    } catch (AssertionError | Exception e) {
                        setText("Test failed.");
                        AppLog.e(getContext(), e);
                    }
                }

            });
        }
    }
}
