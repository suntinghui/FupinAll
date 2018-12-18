package com.jkrm.fupin.bean;

import java.util.List;

/**
 * Created by hzw on 2018/10/27.
 */

public class OssListObjectResultBean {

    private List<String> folderList;
    private List<String> objectList;

    public List<String> getFolderList() {
        return folderList;
    }

    public void setFolderList(List<String> folderList) {
        this.folderList = folderList;
    }

    public List<String> getObjectList() {
        return objectList;
    }

    public void setObjectList(List<String> objectList) {
        this.objectList = objectList;
    }
}
