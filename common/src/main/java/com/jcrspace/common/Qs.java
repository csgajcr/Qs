package com.jcrspace.common;

import com.jcrspace.common.utils.QsThreadPool;

import java.util.concurrent.ExecutorService;

/**
 * Created by jiangchaoren on 2017/2/27.
 */

public class Qs {
    public static final ExecutorService threadPool = QsThreadPool.newPauseThreadPool();
}
