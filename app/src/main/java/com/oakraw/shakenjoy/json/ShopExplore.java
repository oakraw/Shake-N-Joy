package com.oakraw.shakenjoy.json;

import java.util.List;

/**
 *
 */
public class ShopExplore {
    public List<Groups> groups;

    public class Groups{
        public List<Items> items;

        public class Items{
            public Venue venue;

            public class Venue{
                public String id;
            }
        }
    }
}
