package com.example.codingnatorpoject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.codingnatorpoject.databinding.FragmentLastResultBinding
import com.example.codingnatorpoject.DBConnection.UserManager

class LastResultFragment : Fragment() {
    private var totalCorrect: Int? = null  //번들로 받아온 전체 맞은개수를 세기위한 것
    private var chapterNumber: Int? = null  //번들에서 받아온 해당 챕터

    private var order: Int? = null  //번들에서 받아온 10챕터의 order수로 chapterNumber를 파악한다 .. 11이 넘어온다

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            totalCorrect = it.getInt("totalCorrect")
            chapterNumber = it.getInt("chapterNumber")
            order = it.getInt("order")
        }
    }

    var binding: FragmentLastResultBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLastResultBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 10스테이지의 경우, ChapterTenResultFragment에서 적절한 처리 후 유저 데이터를 이미 업데이트한 상태임
        Log.i("LastResultFrag", "Called by Chapter" + chapterNumber!!)
        if (chapterNumber!! != 10)
            UserManager.updateStarAt(1, chapterNumber!!, totalCorrect!!.toByte())

        binding?.btnRestart?.setOnClickListener {
            val restart = "restart"
            val bundle = Bundle().apply {
                putString("restart", restart)
                putInt("totalCorrect", totalCorrect!!)
                putInt("chapterNumber", chapterNumber!!)
            }  //이 변수는 챕터10을 다시 실행했을 경우를 대비한 예비책입니다. 이 변수를 이용해 챕터10을 다시 클릭했을때, totalCorrect를 0으로 초기화 할 수 있습니다.
            findNavController().navigate(R.id.action_lastResultFragment_to_stageOneFragment, bundle)
            //findNavController().popBackStack()
            //이 기능을 넣어줬으니, 이제 클라우드에서 보관되는 점수는 이 LastResultFragment에서 수정되애 함
        }

        binding?.txtNumCorrect?.text = "${totalCorrect}개"
        if(order == 11){
            binding?.txtNumIncorrect?.text = "${10 - (totalCorrect?.toInt() ?: 0)}개"
        }
        else{
            binding?.txtNumIncorrect?.text = "${3 - (totalCorrect?.toInt() ?: 0)}개"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}