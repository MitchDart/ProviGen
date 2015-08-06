package com.tjeannin.provigen.model;

import android.net.Uri;
import com.tjeannin.provigen.annotation.Column;
import com.tjeannin.provigen.annotation.ContentUri;
import com.tjeannin.provigen.annotation.Id;
import com.tjeannin.provigen.annotation.Relation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Contract {

    private String authority;
    private String idField;
    private String tableName;
    private List<ContractField> contractFields;
    private Uri relatedContract;
    private String joinStatement;

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Contract(Class contractClass) {

        contractFields = new ArrayList<ContractField>();

        Field[] fields = contractClass.getFields();
        for (Field field : fields) {

            ContentUri contentUri = field.getAnnotation(ContentUri.class);
            if (contentUri != null) {
                try {
                    Uri uri = (Uri) field.get(null);
                    authority = uri.getAuthority();
                    tableName = uri.getLastPathSegment();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Id id = field.getAnnotation(Id.class);
            if (id != null) {
                try {
                    idField = (String) field.get(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                try {
                    contractFields.add(new ContractField((String) field.get(null), column.value()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Relation relation = field.getAnnotation(Relation.class);
            if(relation != null) {
                try {
                    this.relatedContract = (Uri)field.get(null);
                    this.joinStatement = getTable() + "." + relation.mine() + " = " + relatedContract.getLastPathSegment() + "." + relation.theirs();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getAuthority() {
        return authority;
    }

    public String getIdField() {
        return idField;
    }

    public String getTable() {
        return tableName;
    }

    public Uri getRelatedContract() {
        return relatedContract;
    }

    public String getJoinStatement() {
        return joinStatement;
    }

    public List<ContractField> getFields() {
        return contractFields;
    }
}
