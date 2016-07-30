package com.evento.team2.eventspack.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import java.io.File;

/**
 * Created by daniel-kareski on 7/14/16.
 */
public class MimeTypeResolver {

    public static String getMimeTypeFromUri(String fileUri) {
        if (fileUri == null) {
            return "*/*";
        }
        if (endsWithIgnoreCase(fileUri, ".jpg")) {
            return "image/*";
        }
        if (endsWithIgnoreCase(fileUri, ".jpeg")) {
            return "image/jpeg";
        }
        if (endsWithIgnoreCase(fileUri, ".png")) {
            return "image/*";
        }

        String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(fileUri));
        if (TextUtils.isEmpty(mime)) {
            return mime;
        }

        return "*/*";
    }

    public static void startActivityWithMimeType(Context context, String fileUri) {
        try {
            Intent intent = new Intent();
            String mime = MimeTypeResolver.getMimeTypeFromUri(fileUri);

            File file = new File(fileUri);

            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), mime);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean endsWithIgnoreCase(String str, String suffix) {
        if (str == null || suffix == null) {
            return (str == null && suffix == null);
        }
        if (suffix.length() > str.length()) {
            return false;
        }
        int strOffset = str.length() - suffix.length();
        return str.regionMatches(true, strOffset, suffix, 0, suffix.length());
    }
}
