package com.meetvr.share.control.meetvr;


import com.meetvr.share.info.MpBalanceInfo;

/**
 * Created by wzm-pc on 2016/9/1.
 */
public interface MainInfoUpdate {
    public void OnPubResult(boolean isOk, String live_history_id);
    public void OnMpResult(boolean isOk, MpBalanceInfo mpBalanceInfo);
}
