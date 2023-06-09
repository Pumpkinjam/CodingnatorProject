package com.example.codingnatorpoject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.codingnatorpoject.DBConnection.DatabaseConnector
import com.example.codingnatorpoject.DBConnection.ImageAccessor
import com.example.codingnatorpoject.DBConnection.QuestionRepository
import com.example.codingnatorpoject.databinding.FragmentStageThreeChapterTenQuizOXBinding

//var totalCorrect_three = 0  //전체 맞은 개수를 세기위한 전역변수입니다. 일단 OX가 다 끝나고 four로 이동시에 bundle에 넣어줍니다

class StageThreeChapterTenQuizOXFragment : Fragment() {
    private var totalCorrect: Int? = null  //번들로 받아온 전체 맞은개수를 세기위한 것
    private var order: Int? = null
    private var restart : String? = null //LastResultFragment에서 재시작 신호를 받았을때

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            totalCorrect = it.getInt("totalCorrect")
            order = it.getInt("order")
            restart = it.getString("restart")
        }
    }

    var binding: FragmentStageThreeChapterTenQuizOXBinding? = null
    private val repo = QuestionRepository(activity?.applicationContext)

    /*
    var problems =
        arrayOf( //mapOf를 사용해서 문제를 추출합니다.... 배열의 형태로 만들어줬습니다. 물론, 현재는 무작위 추출이 아니고 이 배열의 순서대로 문제가 출력되는 형식으로 했습니다.
            mapOf(
                "question" to "다음 블록은 캐릭터를 무작위 위치로 이동시키나요?",
                "answer" to "X",
                "example1" to "O",
                "example2" to "X",
                "reason" to "정답 X, ‘마우스 포인터’로 이동하기 블록이므로 캐릭터를 마우스 포인트 위치로 이동시켜요."
            ),
            mapOf(
                "question" to "해당 버튼은 배경 추가 버튼인가요?",
                "answer" to "O",
                "example1" to "O",
                "example2" to "X",
                "reason" to "정답 : O, 해당 버튼은 배경 추가 버튼이 맞아요."
            )
        )
     */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStageThreeChapterTenQuizOXBinding.inflate(inflater)
        return binding?.root
    }

    var question = ""
    var answer = ""
    var example1 = ""
    var example2 = ""
    var reason = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //LastResultFragment에서 재시작으로 넘어왔을 경우, totalCorrect 다시 0로 초기화 합니다.
        if (restart == "restart") totalCorrect = 0

        showProblem(3, 10, order!!)

        binding?.btnChapter10O?.setOnClickListener {
            selectExample(example1, question)
        }

        binding?.btnChapter10X?.setOnClickListener {
            selectExample(example2, question)
        }
    }

    fun showProblem(stage: Int, chapter: Int, pn: Int) { //problemNUmber도 파라미터로 받기(객체지향으로 만들기)
        val problem = repo.get(stage, chapter, pn)

        question = problem["content"].toString()  //즉, question to 머시기를 String으로 바꿔 question에 넣어줍니다.
        answer = problem["answer"].toString()
        example1 = problem["cand1"].toString()
        example2 = problem["cand2"].toString()
        reason = problem["explanation"].toString()  //틀린 이유를 알려줘야 하므로

        binding?.txtChapter10OXQuestion?.text = question  //위에서 만들어준 녀석들을 binding을 통해 화면에 뿌려줍니다.
        binding?.btnChapter10O?.text = example1
        binding?.btnChapter10X?.text = example2

        binding?.imgChapter10OX?.setImageBitmap(repo.getImage(stage, chapter, pn))
    }

    fun selectExample(example: String, question: String) {  //이 함수는 버튼을 클릭했을 때, 사용하는 함수입니다.
        val bundle = Bundle()
        if (answer == example) {  //즉, 사용자가 입력한 값이 정답일때
            totalCorrect  = totalCorrect!! + 1
            bundle.putInt("totalCorrect", totalCorrect!!)  //맞은 개수를 번들에 넣어서 보내준다.
            //bundle.putString("answer", answer)
            //bundle.putString("question", question)
            bundle.putInt("order", order!!)
            findNavController().navigate(R.id.action_stageThreeChapterTenQuizOXFragment_to_stageThreeChapterTenResultFragment, bundle)
        } else {  //즉, 사용자가 입력한 값이 오답일때,
            bundle.putInt("totalCorrect", totalCorrect!!)  //맞은 개수를 번들에 넣어서 보내준다.
            bundle.putString("example", example)
            //bundle.putString("answer", answer)
            //bundle.putString("question", question)
            bundle.putString("reason", reason)  //틀린 이유 알리기
            bundle.putInt("order", order!!)
            findNavController().navigate(R.id.action_stageThreeChapterTenQuizOXFragment_to_stageThreeChapterTenResultFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}