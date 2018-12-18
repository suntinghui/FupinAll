package com.jkrm.fupin.widgets;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by hzw on 2017/11/23.
 */

public class MyCustomDivider extends RecyclerView.ItemDecoration {

    private int space = 0;
    private int leftSpace = 0;
    private int rightSpace = 0;
    private int topSpace = 0;
    private int bottomSpace = 0;
    private boolean isTop = false;

    public MyCustomDivider(int leftSpace, int rightSpace, int topSpace, int bottomSpace) {
        this.leftSpace = leftSpace;
        this.rightSpace = rightSpace;
        this.topSpace = topSpace;
        this.bottomSpace = bottomSpace;
    }

    public MyCustomDivider(int leftSpace, int rightSpace, int topSpace, int bottomSpace, boolean isTop) {
        this.leftSpace = leftSpace;
        this.rightSpace = rightSpace;
        this.topSpace = topSpace;
        this.bottomSpace = bottomSpace;
        this.isTop = isTop;
    }

    public MyCustomDivider(int space, boolean isTop) {
        this.space = space;
        this.isTop = isTop;
    }

    public MyCustomDivider(int space) {
        this.space = space;
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(this.space == 0) {
            outRect.left = this.leftSpace;
            outRect.right = this.rightSpace;
            outRect.bottom = this.topSpace;
            outRect.top = this.bottomSpace;
        } else {
            outRect.left = this.space;
            outRect.right = this.space;
            outRect.bottom = this.space;
            outRect.top = this.space;
        }

        if(this.isTop) {
            if(parent.getChildLayoutPosition(view) == 0) {
                outRect.top = 0;
            }

            outRect.left = 0;
            outRect.right = 0;
        }

    }
}
