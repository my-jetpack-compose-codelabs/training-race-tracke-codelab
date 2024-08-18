/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.racetracker.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

/**
 * This class represents a state holder for race participant.
 */
class RaceParticipant(
    val name: String,
    val maxProgress: Int = 100,
    val progressDelayMillis: Long = 500L,
    private val progressIncrement: Int = 1,
    private val initialProgress: Int = 0
) {
    init {
        require(maxProgress > 0) { "maxProgress=$maxProgress; must be > 0" }
        require(progressIncrement > 0) { "progressIncrement=$progressIncrement; must be > 0" }
    }

    /**
     * Indicates the race participant's current progress
     */
    var currentProgress by mutableStateOf(initialProgress)
        private set

    // 使用suspend声明一个挂起的 run 函数
    // 挂起: 简单来说就是可以暂停,且不会堵塞进程
    suspend fun run() {
        // 当当前进度小于 100,持续的增加进度值
        // 示意: 主线程(while() -> delay() -> 其他任务(不会阻塞) -> currentProgress += progressIncrement)
        while (currentProgress < maxProgress) {
            // 使用 delay() 挂起函数, 模拟比赛中不同的进度间隔, 间隔时间使用progressDelayMillis的值
            // <- 注意在 delay(progressDelayMillis) 左边的行数处有一个箭头的 icon, 表示这是一个挂起点, 函数可能会挂起,稍后再恢复运行
            delay(progressDelayMillis)
            currentProgress += progressIncrement
        }
    }

    /**
     * Regardless of the value of [initialProgress] the reset function will reset the
     * [currentProgress] to 0
     */
    fun reset() {
        currentProgress = 0
    }
}

/**
 * The Linear progress indicator expects progress value in the range of 0-1. This property
 * calculate the progress factor to satisfy the indicator requirements.
 */
val RaceParticipant.progressFactor: Float
    get() = currentProgress / maxProgress.toFloat()
