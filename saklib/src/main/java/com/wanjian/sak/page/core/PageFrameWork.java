package com.wanjian.sak.page.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.wanjian.sak.view.SAKContainerView;

import java.util.Stack;

public class PageFrameWork {

    private Stack<PageRecord> mPagerStack;

    private SAKContainerView mRootContainer;

    private Context mContext;

    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private static PageFrameWork sSingleton = new PageFrameWork();
    }

    public static PageFrameWork getInstance() {
        return SingletonHolder.sSingleton;
    }

    private PageFrameWork() {
        super();
        mPagerStack = new Stack<>();
    }

    public PageRecord getTop() {
        if (mPagerStack.isEmpty()) {
            return null;
        }
        return mPagerStack.peek();
    }

    /**
     * attach以后才能使用
     *
     * @param root
     */
    public void attachRootView(Context context, SAKContainerView root) {
        if (null != mRootContainer) {
            throw new IllegalStateException("attachRootView aleady called by another one !!");
        }

        mRootContainer = root;
        mContext = context;
    }

    public void detachRootView() {
        mRootContainer = null;
        mContext = null;
    }

    public void startPageForResult(PageAction action) {
        PageRecord record = getTop();
        if (null != record) {
            // 旧页面hide
            record.pager.onHide();
            mRootContainer.removeAllViews();
        }

        // 新页面压栈
        PageRecord newRecord = buildRecord(action);
        Pager newPager = newRecord.pager;
        newPager.attachContext(mContext);
        newPager.setRequestCode(action.getRequestCode());
        mPagerStack.push(newRecord);

        // 新页面show
        newPager.onCreate(newRecord.data);
        mRootContainer.addView(newPager.getContentView(), newPager.getLayoutParams());
        newPager.onShow();
    }

    public void finishPage(String token) {
        PageRecord targetRecord = null;
        for (PageRecord record : mPagerStack) {
            if (!TextUtils.isEmpty(token) && TextUtils.equals(token, record.token)) {
                targetRecord = record;
                break;
            }
        }

        PageRecord topRecord = getTop();
        // 旧页面hide
        if (null != targetRecord && targetRecord == topRecord) {
            topRecord.pager.onHide();
            mPagerStack.remove(topRecord);
            mRootContainer.removeAllViews();
        }

        PageRecord newRecord = getTop();
        if (null != newRecord) {
            // 调用onResult
            if (newRecord.isPendingReuslt) {
                newRecord.pager.onPageResult(targetRecord.pager.getRequestCode(), targetRecord.pager.getResultCode(), targetRecord.pager.getResultData());
            }

            // 新页面show
            newRecord.pager.onShow();
            mRootContainer.addView(newRecord.pager.getContentView(), newRecord.pager.getLayoutParams());
        }

        // 旧页面destroy
        if (null != targetRecord) {
            targetRecord.pager.onDestroy();
        }
    }

    private PageRecord buildRecord(PageAction action) {
        Class targetClass = action.getTargetClass();
        if (!Pager.class.isAssignableFrom(targetClass)) {
            throw new IllegalArgumentException("target class must be the subClass of Pager! ");
        }

        Class<? extends Pager> pagerClass = (Class<? extends Pager>) targetClass;
        PageRecord record = new PageRecord();
        Pager newPage = buildPager(pagerClass);
        record.type = pagerClass;
        record.pager = newPage;
        record.data = action.getData();
        record.token = newPage.getToken();

        return record;
    }

    private Pager buildPager(Class<? extends Pager> type) {
        try {
            return type.getConstructor().newInstance();
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException("default constructor can not found !");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
