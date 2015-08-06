package com.tjeannin.provigen.sample;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import com.tjeannin.provigen.ProviGenBaseContract;
import com.tjeannin.provigen.ProviGenOpenHelper;
import com.tjeannin.provigen.ProviGenProvider;
import com.tjeannin.provigen.annotation.Column;
import com.tjeannin.provigen.annotation.Column.Type;
import com.tjeannin.provigen.annotation.ContentUri;
import com.tjeannin.provigen.annotation.Relation;
import com.tjeannin.provigen.helper.TableBuilder;
import com.tjeannin.provigen.model.Constraint;


public class SampleContentProvider extends ProviGenProvider {

    @Override
    public SQLiteOpenHelper openHelper(Context context) {
        return new SQLiteOpenHelper(getContext(), "sampleprovigen", null, 1) {

            @Override
            public void onCreate(SQLiteDatabase database) {
                // Automatically creates table and needed columns.
                new TableBuilder(Place.class)
                        .addConstraint(Place.REF, Constraint.UNIQUE, Constraint.OnConflict.REPLACE)
                        .createTable(database);

                new TableBuilder(Person.class)
                        .createTable(database);

                // Do initial population here.
            }

            @Override
            public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
            }
        };
    }

    @Override
    public Class[] contractClasses() {
        return new Class[]{Person.class, Place.class};
    }

    public static interface Person extends ProviGenBaseContract {

        @Column(Type.INTEGER)
        public static final String AGE = "int";

        @Column(Type.INTEGER)
        public static final String PLACE_REF = "place";

        @Column(Type.TEXT)
        public static final String NAME = "name";

        @ContentUri
        public static final Uri CONTENT_URI = Uri.parse("content://com.tjeannin.provigen.sample/persons");

        @Relation(mine = "place", theirs = "ref")
        public static final Uri RELATED_TO = Place.CONTENT_URI;
    }

    public static interface Place extends ProviGenBaseContract {

        @Column(Type.INTEGER)
        public static final String REF  = "ref";

        @Column(Type.TEXT)
        public static final String NAME = "place_name";

        @ContentUri
        public static final Uri CONTENT_URI = Uri.parse("content://com.tjeannin.provigen.sample/places");
    }


}
