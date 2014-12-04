package com.oakraw.shakenjoy.json;

import java.util.List;

/**
 * Created by Rawipol on 8/30/14 AD.
 */


public class ShopInfo {

    public class Venue{
        public String name;
        public Location location;
        public List<Categories> categories;
        public String canonicalUrl;
        public float rating;
        public Photos photos;

        public class Location{
            public List<String> formattedAddress;
            public double lat;
            public double lng;

            @Override
            public String toString() {
                return lat+","+lng;
            }

            public String getFormattedAddress(){
                StringBuilder address = new StringBuilder();
                for(String a : formattedAddress){
                    address.append(a+" ");
                }
                return address.toString();
            }

        }

        public class Categories{
            public String name;
        }

        public class Photos{
            public List<Groups> groups;

            public class Groups{
                public List<Items> items;

                public class Items{
                    public String prefix;
                    public String suffix;

                }
            }
        }

        public String getCatagories(){
            return categories.get(0).name;
        }
    }

    public Venue venue;

    @Override
    public String toString() {
        return venue.name+" "+venue.location.toString()+" "+venue.rating;
    }


}
