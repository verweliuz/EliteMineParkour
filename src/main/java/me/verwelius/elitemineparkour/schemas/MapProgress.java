package me.verwelius.elitemineparkour.schemas;

public class MapProgress {
    private final String mapId;
    private long startTime = 0;
    private int checkpoint = -1;

    public MapProgress(String mapId) {
        this.mapId = mapId;
    }

    public void setStartTime() {
        startTime = System.currentTimeMillis();
    }

    public void setCheckpoint(int checkpoint) {
        this.checkpoint = checkpoint;
    }

    public String getMapId() {
        return mapId;
    }

    public long getStartTime() {
        return startTime;
    }

    public int getCheckpoint() {
        return checkpoint;
    }
}
