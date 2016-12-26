package com.sharpnode.widget;

import com.sharpnode.model.ApplianceModel;

import java.util.ArrayList;

/**
 * Created by admin on 12/21/2016.
 */

public class WidgetUtils {

    private static ArrayList<ApplianceModel> widgetAppliances = new ArrayList<>();

    public static ArrayList<ApplianceModel> getWidgetAppliances() {
        return widgetAppliances;
    }

    public static void setWidgetAppliances(ArrayList<ApplianceModel> list) {
        widgetAppliances = list;
    }

}
