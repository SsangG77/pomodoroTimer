package com.sangjin.pomodorotimer

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import android.widget.TextView
import java.util.*

class MainActivity : AppCompatActivity() {

    private val minutesTextView : TextView by lazy {
        findViewById(R.id.minutes)
    }
    private val secondsTextView : TextView by lazy {
        findViewById(R.id.seconds)
    }
    private val seekbar :SeekBar by lazy {
        findViewById(R.id.seekbar)
    }
    private var currentCountDownTimer : CountDownTimer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindViews()
    }

    private fun bindViews() {
        seekbar.setOnSeekBarChangeListener( //프로그레스바의 변화를 감지하는 리스너 함수이다.
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    if (p2) {
                        updateTime(p1 * 60 * 1000L)
                    }
                }

                override fun onStartTrackingTouch(p0: SeekBar?) { //프로그레스바의 조작을 시작했을때
                    currentCountDownTimer?.cancel()
                    currentCountDownTimer = null
                }

                override fun onStopTrackingTouch(p0: SeekBar?) { //프로그레스바를 조작하다가 손을 뗐을때
                    p0 ?: return //seekbar가 널러블형태일때는 구문을 실행하지 않고 바로 리턴시킨다.

                    //seekbar의 값을 "분"형태로 바꿔서 'createCountDownTimer'의 인자값으로 넣어준다.
                    currentCountDownTimer = createCountDownTimer(p0.progress * 60 * 1000L)
                    currentCountDownTimer?.start()

                }
            }
        )
    }
    private fun createCountDownTimer(Milliseconds : Long) : CountDownTimer {
        return object : CountDownTimer(Milliseconds, 1000L) { //프로그레스바의 깂만큼 실행되고 1000L마다 onTick함수가 호출된다.
            override fun onTick(p0: Long) {
                updateTime(p0)
                updateSeekbar(p0)
            }

            override fun onFinish() {
                updateSeekbar(0)
                updateTime(0)

            }
        }
    }
    @SuppressLint("SetTextI18n")
    private fun updateTime(Milliseconds: Long) { //onTick의 함수를 타고와서 각각의 텍스트 값에 프로그레스바의 값을 가공해서 넣어준다.
        val remainSeconds = Milliseconds / 1000

        minutesTextView.text = "%02d".format(remainSeconds / 60)
        secondsTextView.text = "%02d".format(remainSeconds % 60)
    }
    private fun updateSeekbar(Milliseconds: Long) { //onTick의 함수를 타고와서 프로그레스바현재값을 처음에 조작한 프로그레스바의 값을 가공해서 넣어준다.
        seekbar.progress = (Milliseconds / 1000 / 60).toInt()
    }
}