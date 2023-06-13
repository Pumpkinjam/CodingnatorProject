package com.example.codingnatorpoject

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.ScrollingMovementMethod
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.codingnatorpoject.databinding.FragmentStageTwoChapterTenResultBinding

class StageTwoChapterTenResultFragment : Fragment() {
    private var myExample: String? = null
    private var reason: String? = null
    private var order: Int? = null
    private var totalCorrect: Int? = null  //번들로 받아온 전체 맞은개수를 세기위한 것

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            totalCorrect = it.getInt("totalCorrect")

            myExample = it.getString("example")
            reason = it.getString("reason")
            order = it.getInt("order")
        }
    }

    var binding: FragmentStageTwoChapterTenResultBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStageTwoChapterTenResultBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val color = Color.rgb(231, 137, 137)
        val bundle = Bundle()  //몇 챕터를 선택할지 이 번들에 넣어서 알려줍니다.

        if(reason != null){  //오답일 경우,
            val str1 = "<$myExample> : 오답입니다.\n"
            val str2 = "$reason"
            val spannable = SpannableString("$str1$str2")
            spannable.setSpan(ForegroundColorSpan(color), str1.length, str1.length + str2.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding?.txtCorrectChapterTen?.setBackgroundColor(Color.rgb(231, 137,137))  //이렇게 rgb를 이용해 background의 색을 바꿉니다
            binding?.txtCorrectChapterTen?.text = "오답!"
            binding?.txtExplainChapterTen?.setText(spannable, TextView.BufferType.SPANNABLE)
            //binding?.txtExplainChapterTen?.text = reason
            binding?.txtExplainChapterTen?.movementMethod = ScrollingMovementMethod.getInstance()
            binding?.btnNextChapterTen?.setOnClickListener {
                order = order!! + 1  //즉, 여기서 order를 하나씩 올려준다.
                bundle.putInt("order", order!!)

                if(order!! < 5){  //챕터10의 ox가 두 문제 뿐이니, 이렇게 만들어줬습니다.
                    bundle.putInt("totalCorrect", totalCorrect!!)  //맞은 개수를 번들에 넣어서 보내준다.
                    findNavController().navigate(R.id.action_stageTwoChapterTenResultFragment_to_stageTwoChapterTenQuizOXFragment, bundle)
                }
                else if (order == 11){  //챕터10이 다 끝났다면...
                    bundle.putInt("totalCorrect", totalCorrect!!)  //맞은 개수를 번들에 넣어서 보내준다.
                    findNavController().navigate(R.id.action_stageTwoChapterTenResultFragment_to_lastResultTwoFragment, bundle)
                }
                else{
                    bundle.putInt("totalCorrect", totalCorrect!!)  //맞은 개수를 번들에 넣어서 보내준다.
                    findNavController().navigate(R.id.action_stageTwoChapterTenResultFragment_to_stageTwoChapterTenQuizFourFragment, bundle)
                }
            }
        }
        else{  //정답일경우
            binding?.txtCorrectChapterTen?.text = "정답!"
            binding?.txtExplainChapterTen?.text = "다음문제로 이동하세요"
            binding?.btnNextChapterTen?.setOnClickListener {
                order = order!! + 1  //즉, 여기서 order를 하나씩 올려준다.
                bundle.putInt("order", order!!)

                if(order!! < 5){  //챕터10의 ox가 두 문제 뿐이니, 이렇게 만들어줬습니다.
                    bundle.putInt("totalCorrect", totalCorrect!!)  //맞은 개수를 번들에 넣어서 보내준다.
                    findNavController().navigate(R.id.action_stageTwoChapterTenResultFragment_to_stageTwoChapterTenQuizOXFragment, bundle)
                }
                else if (order == 11){  //챕터10이 다 끝났다면...
                    bundle.putInt("totalCorrect", totalCorrect!!)  //맞은 개수를 번들에 넣어서 보내준다.
                    findNavController().navigate(R.id.action_stageTwoChapterTenResultFragment_to_lastResultTwoFragment, bundle)
                }
                else{
                    bundle.putInt("totalCorrect", totalCorrect!!)  //맞은 개수를 번들에 넣어서 보내준다.
                    findNavController().navigate(R.id.action_stageTwoChapterTenResultFragment_to_stageTwoChapterTenQuizFourFragment, bundle)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}