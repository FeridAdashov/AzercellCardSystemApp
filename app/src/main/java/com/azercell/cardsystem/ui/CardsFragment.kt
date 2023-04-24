package com.azercell.cardsystem.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import com.azercell.cardsystem.R
import com.azercell.cardsystem.adapter.CardHistoryAdapter
import com.azercell.cardsystem.base.BaseFragment
import com.azercell.cardsystem.common.extensions.verticalLayoutManager
import com.azercell.cardsystem.common.utils.CommonUtils
import com.azercell.cardsystem.data.Constants
import com.azercell.cardsystem.domain.entity.*
import com.azercell.cardsystem.domain.interactors.CardsInteractor
import com.azercell.cardsystem.domain.interactors.TransactionInteractor
import com.azercell.cardsystem.domain.interactors.UserInteractor
import com.azercell.cardsystem.viewModel.*
import com.jama.carouselview.CarouselScrollListener
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.app_bar_block.*
import kotlinx.android.synthetic.main.fragment_cards.*
import javax.inject.Inject

@AndroidEntryPoint
class CardsFragment(
    override val showAppBarLogo: Boolean = true,
    override val withBottomBar: Boolean = true,
    override val showAppBarBackButton: Boolean = false,
    override val showAppBarUsername: Boolean = true,
    override val TAG: String = "CardsFragment"
) : BaseFragment() {

    @Inject
    lateinit var mCardsInteractor: CardsInteractor

    private val mCardsViewModel: CardsViewModel by navGraphViewModels(
        R.id.nav_graph,
        factoryProducer = {
            CardsViewModelProvider(mCardsInteractor)
        }
    )

    @Inject
    lateinit var mTransactionInteractor: TransactionInteractor

    private val mTransactionViewModel: TransactionViewModel by navGraphViewModels(
        R.id.nav_graph,
        factoryProducer = {
            TransactionViewModelProvider(mTransactionInteractor)
        }
    )

    @Inject
    lateinit var mUserInteractor: UserInteractor

    private val mUserViewModel: UserViewModel by navGraphViewModels(
        R.id.nav_graph,
        factoryProducer = {
            UserViewModelProvider(mUserInteractor)
        }
    )

    private val mAdapter: CardHistoryAdapter by lazy {
        CardHistoryAdapter(context = requireContext(), object : CardHistoryAdapter.CardsListener {
            override fun clicked(item: MoneyTransferHistoryEntityItem) {
                Toast.makeText(requireContext(), "Clicked event", Toast.LENGTH_SHORT).show()
            }
        }).apply {
            setHasStableIds(true)
        }
    }

    private var showingCardSecret = false
    private var currentPosition = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_cards, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setupUI()

        setCardsLoadingStatus(true)

        mCardsViewModel.getAllCards()
        mUserViewModel.getUser()
    }

    private fun setupUI() {
        tvTitle.text = getString(R.string.cards)

        recyclerViewHistory.apply {
            layoutManager = requireContext().verticalLayoutManager()
            adapter = mAdapter
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupViews(cards: List<CardEntityItem>) {
        btnIncreaseBalance.setOnClickListener {
            if (carouselCards.size > 0)
                cards[carouselCards.currentItem].id?.let { it1 -> increaseBalance(it1) }
        }
        btnMoneyTransfer.setOnClickListener {
            if (carouselCards.size > 0)
                moneyTransfer(cards, fromCardPosition = carouselCards.currentItem)
        }

        carouselCards.isInvisible = true
        carouselCards.apply {
            size = cards.size
            if (size > 0) {
                if (currentPosition == -1) currentPosition = 0
                cards[currentPosition].id?.let {
                    mTransactionViewModel.getMoneyTransferHistory(it)
                }
            }

            carouselScrollListener = object : CarouselScrollListener {
                override fun onScrollStateChanged(
                    recyclerView: RecyclerView,
                    newState: Int,
                    position: Int
                ) {
                    currentPosition = position
                    cards[position].id?.let { mTransactionViewModel.getMoneyTransferHistory(it) }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                }
            }

            setCarouselViewListener { view, position ->
                val tvPan = view.findViewById<TextView>(R.id.tvCardPan)
                val tvCvv = view.findViewById<TextView>(R.id.tvCVV)
                val imgEye = view.findViewById<ImageView>(R.id.imgEyeShowCardSecretDetail)
                val imgCardType = view.findViewById<ImageView>(R.id.imgViewCardType)
                val imgCardBack = view.findViewById<ImageView>(R.id.imgCardBack)

                if (cards[position].pan16.toString().first() == '4') {
                    imgCardType.setImageResource(R.drawable.ic_visa)
                    imgCardType.isVisible = true
                } else if (cards[position].pan16.toString().first() == '5') {
                    imgCardType.setImageResource(R.drawable.ic_mastercard)
                    imgCardType.isVisible = true
                } else imgCardType.isInvisible = true

                view.findViewById<TextView>(R.id.tvCardName).text = cards[position].cardName
                view.findViewById<TextView>(R.id.tvExpireDate).text = cards[position].expireDate
                view.findViewById<TextView>(R.id.tvCardBalance).text =
                    "${cards[position].balance ?: 0.0} ${cards[position].currency}"
                tvPan.text = cards[position].panMasked
                tvCvv.text = getString(R.string.cvv)
                imgEye.setOnClickListener {
                    showingCardSecret = if (showingCardSecret) {
                        tvPan.text = cards[position].panMasked
                        tvCvv.text = getString(R.string.cvv)
                        imgEye.setImageResource(R.drawable.ic_eye_filled)
                        false
                    } else {
                        tvPan.text = cards[position].pan16
                        imgEye.setImageResource(R.drawable.ic_eye_not_filled)
                        tvCvv.text = cards[position].cvv
                        true
                    }
                }

                if (!cards[position].cardImage.isNullOrEmpty())
                    Picasso.get()
                        .load(cards[position].cardImage)
                        .placeholder(R.drawable.world_map)
                        .into(imgCardBack)
            }

            show()
        }

        if (currentPosition > -1 && cards.isNotEmpty()) {
            carouselCards.currentItem = currentPosition
        }

        Handler(Looper.getMainLooper()).postDelayed({
            setCardsLoadingStatus(false)
        }, 500)
    }

    private fun moneyTransfer(cards: List<CardEntityItem>, fromCardPosition: Int) {
        if (cards.size < 2) {
            mBaseActivity.showToast(getString(R.string.must_have_at_least_two_cards))
            return
        }

        val filteredList =
            cards.filter { it.id != cards[fromCardPosition].id && !it.panMasked.isNullOrEmpty() }

        CommonUtils.showListDialog(
            mContext,
            filteredList.map { it.panMasked!! }.toList(),
            getString(R.string.select_card)
        ) { position ->
            filteredList[position].id?.let { toCardId ->
                CommonUtils.showGetSingleInputDialog(mContext, getString(R.string.type_amount)) {
                    try {
                        val amount = it.toDouble()

                        if ((cards[fromCardPosition].balance ?: 0.0) < amount) {
                            mBaseActivity.showToast(getString(R.string.not_enough_amount))
                            return@showGetSingleInputDialog
                        }

                        cards[fromCardPosition].id?.let { cardId ->
                            mTransactionViewModel.moneyTransfer(
                                cardId,
                                toCardId,
                                amount,
                                "AZN"
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        mBaseActivity.showToast(getString(R.string.wrong_amount))
                    }
                }
            }
        }
    }

    private fun increaseBalance(cardId: Int) {
        CommonUtils.showGetSingleInputDialog(mContext, getString(R.string.type_amount)) {
            try {
                val amount = it.toDouble()
                mTransactionViewModel.increaseCardBalance(cardId, amount)
            } catch (e: Exception) {
                e.printStackTrace()
                mBaseActivity.showToast(getString(R.string.wrong_amount))
            }
        }
    }

    private fun initViewModel() {
        fun errorHappened(it: BaseEntity) {
            hideProgress()

            when (it.code) {
                Constants.UNAUTHORIZED_EXCEPTION -> showDialogWithAction(
                    getString(R.string.error),
                    mCardsViewModel.getErrorMessage(requireContext(), it)
                )
                Constants.UNKNOWN_ERROR -> Log.d(
                    Constants.COMMON_TAG,
                    "$TAG: ${it.message.toString()}"
                )
                else -> showDialogWithAction(
                    getString(R.string.error),
                    mCardsViewModel.getErrorMessage(requireContext(), it)
                )
            }
        }

        mUserViewModel.userLiveData().observe(viewLifecycleOwner) { userEntity ->
            if (userEntity is RequestResult.Success) {
                tvUserName.isVisible = true
                tvUserName.text = userEntity.body.user?.name
            } else if (userEntity is RequestResult.Error)
                errorHappened(BaseEntity(userEntity.code, userEntity.message))
        }

        mTransactionViewModel.increaseCardBalanceLiveData().observe(viewLifecycleOwner) { entity ->
            if (entity is RequestResult.Success) {
                mBaseActivity.showToast(getString(R.string.balance_increased))

                setCardsLoadingStatus(true)
                mCardsViewModel.getAllCards()
            } else if (entity is RequestResult.Error)
                errorHappened(BaseEntity(entity.code, entity.message))
        }

        mTransactionViewModel.moneyTransferHistoryLiveData().observe(viewLifecycleOwner) { entity ->
            if (entity is RequestResult.Success) {
                val history = entity.body.transactions ?: listOf()

                recyclerViewHistory.isGone = history.isEmpty()
                tvNoHistory.isGone = history.isNotEmpty()

                mAdapter.models = history
            } else if (entity is RequestResult.Error)
                errorHappened(BaseEntity(entity.code, entity.message))
        }

        mTransactionViewModel.moneyTransferLiveData().observe(viewLifecycleOwner) { entity ->
            if (entity is RequestResult.Success) {
                setCardsLoadingStatus(true)
                mCardsViewModel.getAllCards()
            } else if (entity is RequestResult.Error)
                errorHappened(BaseEntity(entity.code, entity.message))
        }

        mCardsViewModel.cardsLiveData().observe(viewLifecycleOwner) { cardsEntity ->
            if (cardsEntity is RequestResult.Success<CardsEntity>) {
                val cards = cardsEntity.body.cards ?: listOf()
                if (cards.isEmpty()) {
                    tvNoCard.isVisible = true
                    hasCardViewGroup.isGone = true
                    setCardsLoadingStatus(false)
                } else {
                    tvNoCard.isGone = true
                    hasCardViewGroup.isVisible = true
                    setupViews(cards)
                }
            } else if (cardsEntity is RequestResult.Error) {
                setCardsLoadingStatus(false)
                errorHappened(BaseEntity(cardsEntity.code, cardsEntity.message))
            }
        }
    }

    private fun setCardsLoadingStatus(isLoading: Boolean) {
        progressBarCards?.isVisible = isLoading
        carouselCards?.isInvisible = isLoading
    }

    override fun onPause() {
        super.onPause()
        carouselCards.animation?.cancel()
    }
}