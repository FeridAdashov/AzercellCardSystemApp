package com.azercell.cardsystem.ui

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.azercell.cardsystem.R
import com.azercell.cardsystem.base.BaseFragment
import com.azercell.cardsystem.common.extensions.hideKeyboard
import com.azercell.cardsystem.data.Constants
import com.azercell.cardsystem.domain.entity.BaseEntity
import com.azercell.cardsystem.domain.entity.RequestResult
import com.azercell.cardsystem.domain.interactors.CardsInteractor
import com.azercell.cardsystem.viewModel.CardsViewModel
import com.azercell.cardsystem.viewModel.CardsViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.app_bar_block.*
import kotlinx.android.synthetic.main.credit_card_type_info_back.*
import kotlinx.android.synthetic.main.credit_card_type_info_front.*
import kotlinx.android.synthetic.main.fragment_add_card.*
import javax.inject.Inject


@AndroidEntryPoint
class AddCardFragment(
    override val showAppBarLogo: Boolean = true,
    override val withBottomBar: Boolean = false,
    override val showAppBarBackButton: Boolean = true,
    override val showAppBarUsername: Boolean = false,
    override val TAG: String = "AddCardFragment"
) : BaseFragment() {

    @Inject
    lateinit var mCardsInteractor: CardsInteractor

    private val mCardsViewModel: CardsViewModel by navGraphViewModels(R.id.nav_graph,
        factoryProducer = {
            CardsViewModelProvider(mCardsInteractor)
        })

    private lateinit var mSetRightOut: AnimatorSet
    private lateinit var mSetLeftIn: AnimatorSet
    lateinit var mSetLeftOut: AnimatorSet
    private var mIsBackVisible = false

    private lateinit var mSetRightCardOut: AnimatorSet
    private lateinit var mSetRightCardIn: AnimatorSet
    private lateinit var mSetLeftCardOut: AnimatorSet
    private lateinit var mSetLeftCardIn: AnimatorSet

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_add_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setupUI()
    }

    private fun setupUI() {
        tvTitle.text = getString(R.string.add_card)

        loadAnimations()
        changeCameraDistance()

        appBarBackButton.setOnClickListener {
            if (mIsBackVisible)
                returnToFront()
            else mBaseActivity.onBackPressed()
        }

        clearHintWhenFocus(edtPanFirst, edtPanFirst.hint.toString())
        clearHintWhenFocus(edtPanSecond, edtPanSecond.hint.toString())
        clearHintWhenFocus(edtPanThird, edtPanThird.hint.toString())
        clearHintWhenFocus(edtPanFourth, edtPanFourth.hint.toString())
        clearHintWhenFocus(edtCvv, edtCvv.hint.toString())

        edtPanFirst.doOnTextChanged { text, _, _, _ ->
            if (text?.toString().isNullOrEmpty()) changeLogosVisibility(false)
            else {
                if (text.toString().first() == '5') {
                    changeLogosVisibility(true)
                    imgViewCardTypeLogoFront.setImageResource(R.drawable.ic_mastercard)
                    imgViewCardTypeLogoBack.setImageResource(R.drawable.ic_mastercard)
                    imgViewCardTypeLogoFront.backgroundTintList = null
                    imgViewCardTypeLogoBack.backgroundTintList = null
                } else if (text.toString().first() == '4') {
                    changeLogosVisibility(true)
                    imgViewCardTypeLogoFront.setImageResource(R.drawable.ic_visa)
                    imgViewCardTypeLogoBack.setImageResource(R.drawable.ic_visa)
                    imgViewCardTypeLogoFront.backgroundTintList =
                        ContextCompat.getColorStateList(requireContext(), R.color.white)
                    imgViewCardTypeLogoBack.backgroundTintList =
                        ContextCompat.getColorStateList(requireContext(), R.color.white)
                } else {
                    changeLogosVisibility(false)
                }
            }

            if (text?.length == 4) edtPanSecond.requestFocus()
        }
        edtPanSecond.doOnTextChanged { text, _, _, _ ->
            if (text?.length == 4) edtPanThird.requestFocus()
        }
        edtPanThird.doOnTextChanged { text, _, _, _ ->
            if (text?.length == 4) edtPanFourth.requestFocus()
        }
        edtPanFourth.doOnTextChanged { text, _, _, _ ->
            if (text?.length == 4) edtExprMonth.requestFocus()
        }
        edtExprMonth.doOnTextChanged { text, _, _, _ ->
            if (text?.length == 2) edtExprYear.requestFocus()
        }
        edtExprYear.doOnTextChanged { text, _, _, _ ->
            if (text?.length == 2 && validateFields()) flipCard()
        }
        edtCvv.doOnTextChanged { text, _, _, _ ->
            if (text?.length == 3) {
                btnAddCard.requestFocus()
                mContext.hideKeyboard(edtCvv)
            }
        }

        btnAddCard.setOnClickListener {
            if (validateFields()) if (mIsBackVisible) {
                mCardsViewModel.addCard(
                    "${edtPanFirst.text.toString()}${edtPanSecond.text.toString()}${edtPanThird.text.toString()}${edtPanFourth.text.toString()}",
                    "${edtExprMonth.text.toString()}/${edtExprMonth.text.toString()}",
                    edtCvv.text.toString(),
                    edtUsername.text.toString()
                )
            } else {
                flipCard()
            }
        }

        edtUsername.requestFocus()
    }

    private fun changeLogosVisibility(b: Boolean) {
        imgViewCardTypeLogoFront.isInvisible = !b
        imgViewCardTypeLogoBack.isInvisible = !b
    }

    private fun clearHintWhenFocus(editText: EditText, hint: String) {
        editText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            editText.hint = if (hasFocus) "" else hint
        }
    }

    private fun initViewModel() {
        fun errorHappened(it: BaseEntity) {
            hideProgress()

            when (it.code) {
                Constants.UNAUTHORIZED_EXCEPTION -> showDialogWithAction(
                    getString(R.string.error), mCardsViewModel.getErrorMessage(requireContext(), it)
                )
                Constants.UNKNOWN_ERROR -> Log.d(
                    Constants.COMMON_TAG, "$TAG: ${it.message.toString()}"
                )
                else -> showDialogWithAction(
                    getString(R.string.error), mCardsViewModel.getErrorMessage(requireContext(), it)
                )
            }
        }

        mCardsViewModel.addCardLiveData().observe(viewLifecycleOwner) { baseEntity ->
            hideProgress()

            if (baseEntity is RequestResult.Success<BaseEntity>) {
                mCardsViewModel.getAllCards()
                mBaseActivity.showToast("Added!")
                findNavController().popBackStack()
            } else if (baseEntity is RequestResult.Error) {
                errorHappened(BaseEntity(baseEntity.code, baseEntity.message))
            }
        }
    }

    private fun changeCameraDistance() {
        val distance = 15000
        val scale = resources.displayMetrics.density * distance
        credit_card_front.cameraDistance = scale
        credit_card_back.cameraDistance = scale
    }


    private fun loadAnimations() {
        mSetRightOut =
            AnimatorInflater.loadAnimator(mContext, R.animator.out_animation) as AnimatorSet
        mSetLeftOut =
            AnimatorInflater.loadAnimator(mContext, R.animator.in_animation) as AnimatorSet
        mSetLeftIn = AnimatorInflater.loadAnimator(mContext, R.animator.in_animation) as AnimatorSet
        mSetRightCardOut =
            AnimatorInflater.loadAnimator(mContext, R.animator.card_flip_right_out) as AnimatorSet
        mSetRightCardIn =
            AnimatorInflater.loadAnimator(mContext, R.animator.card_flip_right_in) as AnimatorSet
        mSetLeftCardOut =
            AnimatorInflater.loadAnimator(mContext, R.animator.card_flip_left_out) as AnimatorSet
        mSetLeftCardIn =
            AnimatorInflater.loadAnimator(mContext, R.animator.card_flip_left_in) as AnimatorSet
    }

    private fun returnToFront() {
        if (!mIsBackVisible) return

        mSetLeftCardIn.setTarget(credit_card_front)
        mSetRightOut.setTarget(credit_card_back)
        mSetLeftCardIn.start()
        mSetRightOut.start()

        btnAddCard.text = getString(R.string.next)
        mIsBackVisible = false
    }

    private fun returnToBack() {
        if (mIsBackVisible) return

        mSetLeftCardIn.setTarget(credit_card_back)
        mSetRightOut.setTarget(credit_card_front)
        mSetLeftCardIn.start()
        mSetRightOut.start()

        edtCvv.requestFocus()
        btnAddCard.text = getString(R.string.add_card)
        mIsBackVisible = true
    }

    private fun flipCard() {
        if (mIsBackVisible) returnToFront()
        else returnToBack()
    }

    private fun validateFields(): Boolean {
        if (edtUsername.text?.trim().isNullOrEmpty()) {
            returnToFront()
            edtUsername.requestFocus()
            return false
        }
        if (edtPanFirst.text?.trim()?.length != 4) {
            returnToFront()
            edtPanFirst.requestFocus()
            return false
        }
        if (edtPanSecond.text?.trim()?.length != 4) {
            returnToFront()
            edtPanSecond.requestFocus()
            return false
        }
        if (edtPanThird.text?.trim()?.length != 4) {
            returnToFront()
            edtPanThird.requestFocus()
            return false
        }
        if (edtPanFourth.text?.trim()?.length != 4) {
            returnToFront()
            edtPanFourth.requestFocus()
            return false
        }
        if (edtExprMonth.text?.trim()?.length != 2) {
            returnToFront()
            edtExprMonth.requestFocus()
            return false
        }
        if (edtExprYear.text?.trim()?.length != 2) {
            returnToFront()
            edtExprYear.requestFocus()
            return false
        }
        if (edtCvv.text?.trim()?.length != 3) {
            returnToBack()
            edtCvv.requestFocus()
            return false
        }

        return true
    }
}