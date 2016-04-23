package com.fandb.twovietimes;

/**
 * Created by Steve on 4/22/2016.
 */

import java.util.Arrays;

//The way Gson (Google JSON) works is that it tries to convert a string to an
//object template which is passed to fromJson() where the template class variables
//are populated, which is why you see this rabbit hole of classes.
//NOTE: If a field is missing there is no warning/error generated. Yay!
//NOTE: I used the exact casing as provided by the api to avoid confusion.

//Doc page: http://developer.tmsapi.com/io-docs

//Oh god...
public class MovieTemplate {
    private class qualityRating{
        @Override
        public String toString() {
            return "qualityRating [ratingsBody=" + ratingsBody + ", value=" + value + "]";
        }
        private String ratingsBody;
        private String value;
    }

    private class ratings{

        private String body;
        private String code;
        @Override
        public String toString() {
            return "ratings [body=" + body + ", code=" + code + "]";
        }
    }

    private class preferredImage{
        @Override
        public String toString() {
            return "preferredImage [width=" + width + ", height=" + height + ", caption=" + caption + ", uri=" + uri
                    + ", category=" + category + ", text=" + text + ", primary=" + primary + "]";
        }
        //Yo dawg I heard you like classes
        private class caption{
            @Override
            public String toString() {
                return "caption [content=" + content + ", lang=" + lang + "]";
            }
            private String content;
            private String lang;
        }

        private String width;
        private String height;
        private caption caption;
        private String uri;
        private String category;
        private String text;
        private String primary;
    }

    private class showtimes{
        @Override
        public String toString() {
            return "showtimes [showtimes=" + showtimes + ", dateTime=" + dateTime + ", quals=" + quals + ", barg="
                    + barg + ", ticketURI=" + ticketURI + "]";
        }
        private class theatre{
            @Override
            public String toString() {
                return "theatre [id=" + id + ", name=" + name + "]";
            }
            private String id;
            private String name;
        }

        private showtimes showtimes;
        private String dateTime;
        private String quals;
        private String barg;
        private String ticketURI;
    }

    private String tmsId;
    private String rootId;
    private String subType;
    private String title;
    private String releaseYear;
    private String releaseDate;
    private String titleLang;
    private String descriptionLang;
    private String entityType;
    private String[] genres;
    private String longDescription;
    private String shortDescrption;
    private String[] topCast;
    private String[] directors;
    private String officalUrl;
    private qualityRating qualityRating;
    private ratings[] ratings;
    private String[] advisories;
    private String runTime;
    private preferredImage preferredImage;
    private showtimes[] showtimes;

    @Override
    public String toString() {
        return "Movie [tmsId=" + tmsId + ", rootId=" + rootId + ", subType=" + subType + ", title=" + title
                + ", releaseYear=" + releaseYear + ", releaseDate=" + releaseDate + ", titleLang=" + titleLang
                + ", descriptionLang=" + descriptionLang + ", entityType=" + entityType + ", genres="
                + Arrays.toString(genres) + ", longDescription=" + longDescription + ", shortDescrption="
                + shortDescrption + ", topCast=" + Arrays.toString(topCast) + ", directors="
                + Arrays.toString(directors) + ", officalUrl=" + officalUrl + ", qualityRating=" + qualityRating
                + ", ratings=" + Arrays.toString(ratings) + ", advisories=" + Arrays.toString(advisories) + ", runTime="
                + runTime + ", preferredImage=" + preferredImage + ", showtimes=" + Arrays.toString(showtimes) + "]";
    }
}