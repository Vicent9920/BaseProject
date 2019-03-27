package com.vincent.dialoglibrary


import android.app.Dialog
import androidx.fragment.app.FragmentActivity
import com.vincent.dialoglibrary.widget.LoopView
import java.util.*


/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2018/12/17
 * desc   : 日期选择对话框
 */
private const val START_YEAR = 2012
private const val EDN_YEAR = 2022

object DateDialog : AbsLooperDialog() {

    class Builder(activity: FragmentActivity) : AbsLooperDialog.Builder(activity),
        LoopView.LoopScrollListener {

        private val mYearView: LoopView
        private val mMonthView: LoopView
        private val mDayView: LoopView

        var mListener: OnListener? = null

        init {

            // 生产年份
            val yearList = arrayListOf<String>()
            for (i in START_YEAR..EDN_YEAR) {
                yearList.add(i.toString() + " " + activity.getString(R.string.dialog_date_year))
            }

            // 生产月份
            val monthList = mutableListOf<String>()
            for (i in 1..12) {
                monthList.add(i.toString() + " " + activity.getString(R.string.dialog_date_month))
            }

            mYearView = createLoopView()
            mMonthView = createLoopView()
            mDayView = createLoopView()

            mYearView.setData(yearList)
            mMonthView.setData(monthList)

            mYearView.setLoopListener(this)
            mMonthView.setLoopListener(this)

            val calendar = Calendar.getInstance()
            mYearView.setInitPosition(calendar.get(Calendar.YEAR) - START_YEAR)
            mMonthView.setInitPosition(calendar.get(Calendar.MONTH))
            mDayView.setInitPosition(calendar.get(Calendar.DAY_OF_MONTH) - 1)
        }

        override fun onItemSelect(loopView: LoopView, position: Int) {
            // 获取这个月最多有多少天
            val calendar = Calendar.getInstance(Locale.CHINA)
            if (loopView == mYearView) {
                calendar.set(START_YEAR + mYearView.selectedItem, mMonthView.selectedItem, 1)
            } else if (loopView == mMonthView) {
                calendar.set(START_YEAR + mYearView.selectedItem, mMonthView.selectedItem, 1)
            }

            val day = calendar.getActualMaximum(Calendar.DATE)

            val dayList = mutableListOf<String>()
            for (i in 1..day) {
                dayList.add(i.toString() + " " + activity.getString(R.string.dialog_date_day))
            }

            mDayView.setData(dayList)
        }


        override fun onConfirm() {
            mListener?.onSelected(
                mDialog, START_YEAR + mYearView.selectedItem,
                mMonthView.selectedItem + 1, mDayView.selectedItem + 1
            )
            dismiss()
        }

        override fun onCancel() {
            mListener?.onCancel(mDialog)
            dismiss()
        }
    }

    interface OnListener {

        /**
         * 选择完日期后回调
         *
         * @param year              年
         * @param month             月
         * @param day               日
         */
        fun onSelected(dialog: Dialog, year: Int, month: Int, day: Int)

        /**
         * 点击取消时回调
         */
        fun onCancel(dialog: Dialog)
    }


}