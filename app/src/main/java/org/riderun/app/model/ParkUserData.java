package org.riderun.app.model;

public class ParkUserData implements PrimaryKey<Integer> {
    private final int rcdbId;
    private Boolean liked;
    private String comment;

    public ParkUserData(int rcdbId) {
        this(rcdbId, null, null);
    }
    public ParkUserData(int rcdbId, Boolean liked, String comment) {
        this.rcdbId = rcdbId;
        this.liked = liked;
        this.comment = comment;
    }

    public int rcdbId() {
        return rcdbId;
    }

    public Boolean getLiked() {
        return liked == null ? false : liked;
    }

    public Boolean getLikedTristate() {
        return liked;
    }

    public String getComment() {
        return comment;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public Integer pk() {
        return null;
    }
}
