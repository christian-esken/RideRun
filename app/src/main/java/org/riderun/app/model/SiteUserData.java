package org.riderun.app.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Private user data for a site, e.g. a comment or a "like".
 * Note: A "site" is a location like a "theme park" or "world heritage site"
 */
public class SiteUserData implements PrimaryKey<Integer> {
    private final String provider;
    private final String siteId;
    private Boolean liked;
    private String comment;
    private Instant lastModified;

    public SiteUserData(String provider, String siteId) {
        this(provider, siteId, null, null, null);
    }
    public SiteUserData(String provider, String siteId, Boolean liked, String comment, Instant lastModified) {
        this.provider = provider;
        this.siteId = siteId;
        this.liked = liked;
        this.comment = comment;
        this.lastModified = lastModified;
    }

    public String iteId() {
        return siteId;
    }

    public boolean getLiked() {
        return liked == null ? false : liked;
    }

    public Boolean getLikedTristate() {
        return liked;
    }

    public String getComment() {
        return comment;
    }

    public Instant getLastModified() {
        return lastModified;
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

    /*
     * equals and hashCode only look for the primary key (provider + key)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SiteUserData that = (SiteUserData) o;
        if (!provider.equals(that.provider)) {
            return false;
        }
        return siteId.equals(that.siteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(provider, siteId);
    }
}
