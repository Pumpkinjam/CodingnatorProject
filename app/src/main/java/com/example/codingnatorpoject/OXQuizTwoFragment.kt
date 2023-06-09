package com.example.codingnatorpoject

import android.app.AlertDialog
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.codingnatorpoject.DBConnection.DatabaseConnector
import com.example.codingnatorpoject.DBConnection.ImageAccessor
import com.example.codingnatorpoject.DBConnection.QuestionRepository
import com.example.codingnatorpoject.databinding.FragmentOXQuizTwoBinding

class OXQuizTwoFragment : Fragment() {
    private var chapterNumber: Int? = null
    private var totalCorrect: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            chapterNumber = it.getInt("chapterNumber")
            totalCorrect = it.getInt("totalCorrect")
        }
    }
    var binding: FragmentOXQuizTwoBinding? = null
    private val repo = QuestionRepository(activity?.applicationContext)

    /*
    var problems =
        arrayOf( //mapOf를 사용해서 문제를 추출합니다.... 배열의 형태로 만들어줬습니다. 물론, 현재는 무작위 추출이 아니고 이 배열의 순서대로 문제가 출력되는 형식으로 했습니다.
            mapOf(
                "question" to "캐릭터를 움직이려고 하는데 이 블록이 어디로 움직여 주는지 알고 싶어. 어디로 움직일까?",
                "answer" to "마우스 포인터",
                "example1" to "무작위 위치",
                "example2" to "마우스 포인터",
                "hint" to "개념에서 칸에 ‘무작위 위치’라고 적혀 있을 때 무작위 위치로 움직인다고 배웠어요. 문제에서 칸에 무엇이 적혀있나요?"
            ),
            mapOf(
                "question" to "해당 블록은 캐릭터가 바라보는 방향이 마우스 포인터가 되게 만드나요?",
                "answer" to "O",
                "example1" to "O",
                "example2" to "X",
                "hint" to "90도 방향보기는 캐릭터가 바라보는 방향이 90도가 된답니다"
            ),
            mapOf(
                "question" to "고양이에게 소리를 추가하고 싶어요. 이 블록이 ‘야옹’이 재생되는 게 맞나요?",
                "answer" to "X",
                "example1" to "O",
                "example2" to "X",
                "hint" to "재생 블록은 칸 안에 있는 소리를 재생해요."
            ),
            mapOf(
                "question" to "현재 과일의 모양이 ‘사과’일 때, 아래 블록을 실행시키면 과일의 모양이 바뀌나요?",
                "answer" to "O",
                "example1" to "O",
                "example2" to "X",
                "hint" to "현재 모양하고 바뀌는 모양하고 다르면 대상의 모양이 바뀌어요"
            ),
            mapOf(
                "question" to "기차가 열차 승강장에 들어오고 있어요, 이게 ‘기차’ 신호를 보내는 블록이 맞나요?",
                "answer" to "O",
                "example1" to "O",
                "example2" to "X",
                "hint" to "□ 신호 보내기는 작동하면 □ 신호를 보내줘요!"
            ),
            mapOf(
                "question" to "민수가 문제를 고민하고 있어요. 여러분이 도와주세요. 아래 조건은 맞나요?",
                "answer" to "O",
                "example1" to "O",
                "example2" to "X",
                "hint" to "~가 아니다 블록은 안쪽 조건의 만족 / 불만족을 반대로 바꿔줘요!"
            ),
            mapOf(
                "question" to "초록 깃발을 클릭하고 말하기 블록이 작동했을 때, 캐릭터는 마우스 포인터와 충돌했나요?",
                "answer" to "O",
                "example1" to "O",
                "example2" to "X",
                "hint" to "~ 까지 기다리기 블록은 조건을 만족할 때까지 기다려요."
            ),
            mapOf(
                "question" to "다음 블록은 캐릭터의 크기를 원래대로 돌려주나요?",
                "answer" to "O",
                "example1" to "O",
                "example2" to "X",
                "hint" to "캐릭터의 원래 크기는 100%에요."
            ),
            mapOf(
                "question" to "다음 블록은 캐릭터를 안 보이게 해주나요?",
                "answer" to "O",
                "example1" to "O",
                "example2" to "X",
                "hint" to "캐릭터를 보이게 해주는 블록은 ‘보이기’ 이랍니다."
            )
        )
*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOXQuizTwoBinding.inflate(inflater)
        return binding?.root
    }

    var question = ""
    var answer = ""
    var example1 = ""
    var example2 = ""
    var hint = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showProblem(2, chapterNumber!!, 1)

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

        if(chapter == 1){
            binding?.btnO?.textSize = 25F
            binding?.btnX?.textSize = 25F
        }

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
            findNavController().navigate(R.id.action_OXQuizTwoFragment_to_resultTwoFragment, bundle)
        } else {  //즉, 사용자가 입력한 값이 오답일때,
            bundle.putString("example", example)
            bundle.putString("answer", answer)
            bundle.putString("question", question)
            bundle.putInt("chapterNumber", chapterNumber!!)
            bundle.putInt("totalCorrect", totalCorrect!!)  //맞은 개수를 번들에 넣어서 보내준다.
            findNavController().navigate(R.id.action_OXQuizTwoFragment_to_resultTwoFragment, bundle)
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