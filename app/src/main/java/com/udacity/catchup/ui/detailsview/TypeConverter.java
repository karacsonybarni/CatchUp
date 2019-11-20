package com.udacity.catchup.ui.detailsview;

import androidx.annotation.Nullable;

import com.udacity.catchup.data.entity.comment.Comment;
import com.udacity.catchup.data.entity.comment.CommentsDataElem;
import com.udacity.catchup.data.entity.comment.PageSection;

import java.util.ArrayList;
import java.util.List;

class TypeConverter {

    @Nullable
    static List<Comment> toComments(List<PageSection> pageSections) {
        List<CommentsDataElem> dataElems =
                pageSections != null ? pageSections.get(1).getData().getChildren() : null;
        if (dataElems == null || dataElems.size() == 0) {
            return null;
        }

        List<Comment> commentList = new ArrayList<>();
        for (CommentsDataElem dataElem : dataElems) {
            commentList.add(dataElem.getComment());
        }
        return commentList;
    }
}
