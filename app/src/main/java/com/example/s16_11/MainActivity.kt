package com.example.s16_11

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Locale.TAIWAN


data class RaceResult(
    var winner:String,
    var rabbit:Int,
    var turtle:Int,
    var timestamps:String
)


class MainActivity : AppCompatActivity() {

    private lateinit var adapter: RaceResultAdapter
    private val raceResults = mutableListOf<RaceResult>()
    private lateinit var rvHistory: RecyclerView

    // 建立兩個數值，用於計算兔子與烏龜的進度
    private var progressRabbit = 0
    private var progressTurtle = 0
    // 建立變數以利後續綁定元件
    private lateinit var btnStart: Button
    private lateinit var sbRabbit: SeekBar
    private lateinit var sbTurtle: SeekBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rvHistory = findViewById(R.id.recyclerView1)
        rvHistory.layoutManager = LinearLayoutManager(this)
        adapter = RaceResultAdapter(raceResults)
        rvHistory.adapter = adapter

        // 將變數與XML元件綁定
        btnStart = findViewById(R.id.btnStart)
        sbRabbit = findViewById(R.id.sbRabbit)
        sbTurtle = findViewById(R.id.sbTurtle)
        // 對開始按鈕設定監聽器
        btnStart.setOnClickListener {
            // 進行賽跑後按鈕不可被操作
            btnStart.isEnabled = false
            // 初始化兔子的賽跑進度
            progressRabbit = 0
            // 初始化烏龜的賽跑進度
            progressTurtle = 0
            // 初始化兔子的SeekBar進度
            sbRabbit.progress = 0
            // 初始化烏龜的SeekBar進度
            sbTurtle.progress = 0
            // 兔子起跑
            runRabbit()
            // 烏龜起跑
            runTurtle()
        }
    }

    // 建立 showToast 方法顯示Toast訊息
    private fun showToast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    // 建立 Handler 變數接收訊息
    private val handler = Handler(Looper.getMainLooper()) { msg ->
        // 判斷編號，並更新兔子的進度
        if (msg.what == 1) {
            // 更新兔子的進度
            sbRabbit.progress = progressRabbit
            // 若兔子抵達，則顯示兔子勝利
            if (progressRabbit >= 100 && progressTurtle < 100) {
                val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
                dateFormat.timeZone = java.util.TimeZone.getTimeZone("Asia/Taipei")
                val currentTime = dateFormat.format(Date())
                recordResult("兔子", progressRabbit, progressTurtle, currentTime)
                btnStart.isEnabled = true
            }

        } else if (msg.what == 2) {
            // 更新烏龜的進度
            sbTurtle.progress = progressTurtle
            // 若烏龜抵達，則顯示烏龜勝利
            if (progressTurtle >= 100 && progressRabbit < 100) {
                val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
                dateFormat.timeZone = java.util.TimeZone.getTimeZone("Asia/Taipei")
                val currentTime = dateFormat.format(Date())
                recordResult("烏龜", progressRabbit, progressTurtle, currentTime)
                btnStart.isEnabled = true
            }
        }
        true
    }

    // 用 Thread 模擬兔子移動
    private fun runRabbit() {
        Thread {
            // 兔子有三分之二的機率會偷懶
            val sleepProbability = arrayOf(true, true, false)
            while (progressRabbit < 100 && progressTurtle < 100) {
                try {
                    Thread.sleep(100) // 延遲0.1秒更新賽況
                    if (sleepProbability.random())
                        Thread.sleep(300) // 兔子偷懶0.3秒
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                progressRabbit += 3 // 每次跑三步

                val msg = Message() // 建立Message物件
                msg.what = 1  // 加入編號
                handler.sendMessage(msg) // 傳送兔子的賽況訊息
            }
        }.start() // 啟動 Thread
    }

    // 用 Thread 模擬烏龜移動
    private fun runTurtle() {
        Thread {
            while (progressTurtle < 100 && progressRabbit < 100) {
                try {
                    Thread.sleep(100) // 延遲0.1秒更新賽況
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                progressTurtle += 1 // 每次跑一步

                val msg = Message() // 建立Message物件
                msg.what = 2 // 加入編號
                handler.sendMessage(msg) // 傳送烏龜的賽況訊息
            }
        }.start() // 啟動 Thread
    }

    private fun recordResult(winner: String, rabbittimes: Int, turtletimes: Int, timestamp: String) {
        //val sdf = java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss", java.util.Locale.getDefault())
        //val timestamp = sdf.format(java.util.Date())

        val result = RaceResult(
            winner = winner,
            rabbit = rabbittimes,
            turtle = turtletimes,
            timestamps = timestamp
        )

        raceResults.add(0, result) // 加在最上面
        adapter.notifyItemInserted(0)
        rvHistory.scrollToPosition(0)
    }

}