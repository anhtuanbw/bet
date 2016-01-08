package vn.kms.ngaythobet.web.dto;

import java.util.List;

public class Comment {
    private List<CommentInfo> commentsInfo;
    private List<HistoryBetting> historiesBetting;

    public List<CommentInfo> getCommentsInfo() {
        return commentsInfo;
    }

    public void setCommentsInfo(List<CommentInfo> commentsInfo) {
        this.commentsInfo = commentsInfo;
    }

    public List<HistoryBetting> getHistoriesBetting() {
        return historiesBetting;
    }

    public void setHistoriesBetting(List<HistoryBetting> historiesBetting) {
        this.historiesBetting = historiesBetting;
    }
}
