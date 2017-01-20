package com.mycollection.rakesh.mycollection.util;

import android.content.Context;
import android.graphics.Typeface;

public class FontManager {

	public static final String PATH_OPENSANS_LIGHT = "fonts/OpenSans/OpenSans-Light.ttf";
	public static final String PATH_OPENSANS_SEMIBOLD = "fonts/OpenSans/OpenSans-Semibold.ttf";
	public static final String PATH_OPENSANS_ITALIC = "fonts/OpenSans/OpenSans-Italic.ttf";
	public static final String PATH_OPENSANS_BOLD = "fonts/OpenSans/OpenSans-Bold.ttf";
	public static final String PATH_OPENSANS_REGULAR = "fonts/OpenSans/OpenSans-Regular.ttf";
	public static final String PATH_OPENSANS_BOLDITALIC = "fonts/OpenSans/OpenSans-BoldItalic.ttf";

	public static final int OPENSANS_LIGHT = 0;
	public static final int OPENSANS_SEMIBOLD = 1;
	public static final int OPENSANS_ITALIC = 2;
	public static final int OPENSANS_BOLD = 3;
	public static final int OPENSANS_REGULAR = 4;
	public static final int OPENSANS_BOLDITALIC = 5;
	
	private static final int NUM_OF_CUSTOM_FONTS = 6;

	private static boolean fontsLoaded = false;

	private static Typeface[] fonts = new Typeface[NUM_OF_CUSTOM_FONTS];

	private static String[] fontPath = { PATH_OPENSANS_LIGHT,
			PATH_OPENSANS_SEMIBOLD, PATH_OPENSANS_ITALIC, PATH_OPENSANS_BOLD,
			PATH_OPENSANS_REGULAR, PATH_OPENSANS_BOLDITALIC

	};

	public static Typeface getTypeface(Context context, int fontIdentifier) {
		if (!fontsLoaded) {
			loadAllFonts(context);
		}
		return fonts[fontIdentifier];
	}

	private static void loadAllFonts(Context context) {
		for (int i = 0; i < NUM_OF_CUSTOM_FONTS; i++) {
			fonts[i] = Typeface.createFromAsset(context.getAssets(),
					fontPath[i]);
		}
		fontsLoaded = true;
	}

	public static Typeface getOpenSansSemiBoldFont(Context context) {
		return getTypeface(context, OPENSANS_SEMIBOLD);
	}

	public static Typeface getOpenSansLightFont(Context context) {
		return getTypeface(context, OPENSANS_LIGHT);
	}

	public static Typeface getOpenSansBoldFont(Context context) {
		return getTypeface(context, OPENSANS_BOLD);
	}

	public static Typeface getOpenSansItalicFont(Context context) {
		return getTypeface(context, OPENSANS_ITALIC);
	}

	public static Typeface getOpenSansRegularFont(Context context) {
		return getTypeface(context, OPENSANS_REGULAR);
	}
	
	public static Typeface getOpenSansBoldItalicFont(Context context) {
		return getTypeface(context, OPENSANS_BOLDITALIC);
	}

}
