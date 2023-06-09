package com.example.codingnatorpoject

import android.app.AlertDialog
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
//import com.davemorrissey.labs.subscaleview.ImageSource
import com.example.codingnatorpoject.DBConnection.DatabaseConnector
import com.example.codingnatorpoject.DBConnection.ImageAccessor
import com.example.codingnatorpoject.DBConnection.QuestionRepository
import com.example.codingnatorpoject.databinding.FragmentOXQuizBinding

class OXQuizFragment : Fragment() {
    private var chapterNumber: Int? = null
    private var totalCorrect: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            chapterNumber = it.getInt("chapterNumber")
            totalCorrect = it.getInt("totalCorrect")
        }
    }

    var binding: FragmentOXQuizBinding? = null
    private val repo = QuestionRepository(activity?.applicationContext)

    /*
    var problems =
        arrayOf( //mapOf를 사용해서 문제를 추출합니다.... 배열의 형태로 만들어줬습니다. 물론, 현재는 무작위 추출이 아니고 이 배열의 순서대로 문제가 출력되는 형식으로 했습니다.
            mapOf(
                "question" to "해당 블록은 캐릭터가 바라보는 방향으로 30만큼 움직이나요?",
                "answer" to "O",
                "example1" to "O",
                "example2" to "X",
                "hint" to "개념에서 빈칸에 ‘10’이라고 적혀 있을 때 10만큼 움직인다고 배웠어요. 문제에서 빈칸에 얼마가 적혀있나요?"
            ),
            mapOf(
                "question" to "해당 블록은 캐릭터가 바라보는 방향이 90도가 되게 만드나요?",
                "answer" to "X",
                "example1" to "O",
                "example2" to "X",
                "hint" to "90도 방향보기는 캐릭터가 바라보는 방향이 90도가 된답니다"
            ),
            mapOf(
                "question" to "고양이가 움직이고 싶어해요. 아래 블록은 스페이스 키를 눌렀을 때 고양이가 10만큼 움직이도록 만드나요?",
                "answer" to "O",
                "example1" to "O",
                "example2" to "X",
                "hint" to "□ 키를 눌렀을 때는 □ 키를 누르면 그 아래의 블록들이 작동한답니다"
            ),
            mapOf(
                "question" to "호랑이가 움직이려고 해요. 아래 블록은 호랑이를 어느 쪽으로 이동시킬까요?",
                "answer" to "오른쪽",
                "example1" to "왼쪽",
                "example2" to "오른쪽",
                "hint" to "x좌표는 좌우를 담당한다고 했어요. x좌표가 +이면 오른쪽, -면 왼쪽으로 가요"
            ),
            mapOf(
                "question" to "해당 버튼은 캐릭터(스프라이트) 추가 버튼인가요?",
                "answer" to "X",
                "example1" to "O",
                "example2" to "X",
                "hint" to "고양이 모양은 스프라이트 추가 버튼이고, 지구본 모양은 언어 설정 버튼이에요"
            ),
            mapOf(
                "question" to "고양이가 말하고 싶어해요. 아래 블록이 실행되면 고양이는 20을 말하나요?",
                "answer" to "X",
                "example1" to "O",
                "example2" to "X",
                "hint" to "개념에서 빈칸에 ‘안녕!’이라고 적혀 있을 때 ‘안녕!’을 말한다고 배웠어요. 문제에서 빈칸에 무엇이 적혀있나요?"
            ),
            mapOf(
                "question" to "남은 김밥의 수를 수첩에 쓰려고 해요. 아래 블록을 실행시키면 ‘김밥’ 개수는 10으로 정해지나요?",
                "answer" to "X",
                "example1" to "O",
                "example2" to "X",
                "hint" to "개념에서 빈칸에 ‘0’이라고 적혀 있을 때 앞의 변수를 0으로 설정한다고 배웠어요. 문제에서 빈칸에 무엇이 적혀있나요?"
            ),
            mapOf(
                "question" to "아래 조건은 맞나요? 맞으면 O, 틀리면 X를 눌러주세요,",
                "answer" to "X",
                "example1" to "O",
                "example2" to "X",
                "hint" to "10은 50보다 큰가요?"
            ),
            mapOf(
                "question" to "오리가 걸어가는 중이에요. 초록 깃발을 누르고 충분히 기다렸을 때, 오리의 x좌표는 계속 커지나요?",
                "answer" to "X",
                "example1" to "O",
                "example2" to "X",
                "hint" to "스크래치에서 x좌표의 최대값은 캐릭터의 종류 및 크기에 따라 다르지만, 무한히 커지지는 않도록 되어 있어요"
            )
        )
    */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOXQuizBinding.inflate(inflater)
        return binding?.root
    }

    var question = ""
    var answer = ""
    var example1 = ""
    var example2 = ""
    var hint = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showProblem(1, chapterNumber!!, 1)

        binding?.btnO?.setOnClickListener {
            selectExample(example1, question)
        }

        binding?.btnX?.setOnClickListener {
            selectExample(example2, question)
        }

        binding?.btnHintOX?.setOnClickListener {
            showHintBox(hint)
        }

    }

    fun showProblem(stage: Int, chapter: Int, pn: Int) { //problemNUmber도 파라미터로 받기(객체지향으로 만들기)
        val problem = repo.get(stage, chapter, pn)

        question = problem["content"].toString()  //즉, question to 머시기를 String으로 바꿔 question에 넣어줍니다.
        answer = problem["answer"].toString()
        example1 = problem["cand1"].toString()
        example2 = problem["cand2"].toString()
        hint = problem["hint"]?: "No hint."

        binding?.txtQuestion?.text = question  //위에서 만들어준 녀석들을 binding을 통해 화면에 뿌려줍니다.
        binding?.txtQuestion?.movementMethod = ScrollingMovementMethod.getInstance()  //이제 qustion텍스트도 스크롤이 가능합니다.
        binding?.btnO?.text = example1
        binding?.btnX?.text = example2

        binding?.imgQuestionOX?.setImageBitmap(repo.getImage(stage, chapter, pn))

        if(pn == 4 || pn ==8){
            binding?.btnO?.textSize = 40F
            binding?.btnX?.textSize = 40F
        }

    }

    fun selectExample(example: String, question: String) {  //이 함수는 버튼을 클릭했을 때, 사용하는 함수입니다.
        val bundle = Bundle()
        if (answer == example) {  //즉, 사용자가 입력한 값이 정답일때
            totalCorrect = totalCorrect!! + 1
            bundle.putString("answer", answer)
            bundle.putString("question", question)
            bundle.putInt("chapterNumber", chapterNumber!!)
            bundle.putInt("totalCorrect", totalCorrect!!)  //맞은 개수를 번들에 넣어서 보내준다.
            findNavController().navigate(R.id.action_OXQuizFragment_to_resultFragment, bundle)
        } else {  //즉, 사용자가 입력한 값이 오답일때,
            bundle.putString("example", example)
            bundle.putString("answer", answer)
            bundle.putString("question", question)
            bundle.putInt("chapterNumber", chapterNumber!!)
            bundle.putInt("totalCorrect", totalCorrect!!)  //맞은 개수를 번들에 넣어서 보내준다.
            findNavController().navigate(R.id.action_OXQuizFragment_to_resultFragment, bundle)
        }
    }

    fun showHintBox(hint: String){  //힌트박스를 보여주기 위한 함수입니다.
        val alertDialog = AlertDialog.Builder(this.context)
            .setTitle("힌트")
            .setMessage(hint)
            .setNeutralButton("닫기", null)
            .create()
        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}