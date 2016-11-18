//===============================================================================
// (c) 2015 eWorkplace Apps.  All rights reserved.
// Original Author: Dheeraj Nagar
// Original Date: 30 Nov 2015
//===============================================================================
package com.sharpnode.utils;

/**
 * Enum class to manage the formatting
 */
public class TimeStyleEnum {
    public enum StyleType {

        SHORT(0),
        LONG(1);


        private int id;

        StyleType(int id) {
            this.id = id;
        }

        public static StyleType stringToEnum(String displayString) {
            switch (displayString) {
                case "SHORT":
                    return StyleType.SHORT;
                case "LONG":
                    return StyleType.LONG;
                default:
                    return null;
            }
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            switch (this) {
                case SHORT:
                    return "SHORT";
                case LONG:
                    return "LONG";
                default:
                    return "SYSTEM_ERROR";
            }
        }
    }
}
