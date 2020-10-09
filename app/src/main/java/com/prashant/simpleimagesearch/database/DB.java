package com.prashant.simpleimagesearch.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.prashant.simpleimagesearch.model.ImageDetails;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DB extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "DB.sqlite";
    private static final int DATABASE_VERSION = 1;
    private static final String comments = "comments";

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public ImageDetails getCommentById(String tag, String Id) {
//            ArrayList<ImageDetails> data=new ArrayList<ImageDetails>();
        ImageDetails data = new ImageDetails();
        try {
            Cursor cursor = null;
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("SELECT * FROM comments WHERE " + tag + " = ? ", new String[]{Id});
            if (cursor.moveToFirst()) {
                do {
                    ImageDetails imageDetails = new ImageDetails();
                    imageDetails.setItemId(cursor.getString(1));
                    imageDetails.setTitle(cursor.getString(2));
                    imageDetails.setLink(cursor.getString(3));
                    imageDetails.setComment(cursor.getString(4));
                    data = imageDetails;
                } while (cursor.moveToNext());

                cursor.close();
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return data;
    }

    public boolean AddComment(ImageDetails imageDetails) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues conV = new ContentValues();
            conV.put("ItemId", imageDetails.getId());
            conV.put("Title", imageDetails.getTitle());
            conV.put("Link", imageDetails.getLink());
            conV.put("Comment", imageDetails.getComment());

            Cursor cursor = db.rawQuery("SELECT * FROM comments WHERE ItemId = ? ", new String[]{imageDetails.getId()});
            if (cursor.moveToFirst()) {
                String where = "ItemId = ?";
                db.update(comments, conV, where, new String[]{imageDetails.getId()});
                return true;

            } else {
                db.insert(comments, null, conV);
                return true;
            }

        } catch (Exception e) {
            e.getMessage();
            return false;
        }
    }


}


