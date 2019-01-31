package com.github.cqu_bdsc.collision_warning_system.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

public class LTEModel extends BaseModel {

    @Column
    private long LTEdelay;

    public void setTimeStamp(long timeStamp) {
        this.LTEdelay = timeStamp;
    }

    public long getTimeStamp() {
        return LTEdelay;
    }

}

