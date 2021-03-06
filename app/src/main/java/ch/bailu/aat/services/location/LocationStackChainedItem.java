package ch.bailu.aat.services.location;


public abstract class LocationStackChainedItem extends LocationStackItem {
    private final LocationStackItem next;
    
    public LocationStackChainedItem(LocationStackItem n) {
        next = n;
    }
    
    @Override
    public void sendLocation(LocationInformation l) {
        next.newLocation(l);
    }
    
    
    @Override
    public void sendState(int state) {
        next.newState(state);
    }
    
    @Override
    public void newState(int state) {
        sendState(state);
    }
}
