package com.photoglyde.justincornelius.photoglyde

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.bartoszlipinski.viewpropertyobjectanimator.ViewPropertyObjectAnimator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.photoglyde.justincornelius.photoglyde.Adapters.BindingHorizontal
import com.photoglyde.justincornelius.photoglyde.Adapters.FeedAdapter
import com.photoglyde.justincornelius.photoglyde.Data.GlobalVals
import com.photoglyde.justincornelius.photoglyde.Data.GlobalVals.currentUser
import com.photoglyde.justincornelius.photoglyde.Data.UserInfo
import com.photoglyde.justincornelius.photoglyde.Networking.ConnectionUtils
import com.photoglyde.justincornelius.photoglyde.Networking.NoConnection
import com.photoglyde.justincornelius.photoglyde.Networking.PostUN
import com.photoglyde.justincornelius.photoglyde.Networking.UserManagement
import kotlinx.android.synthetic.main.activity_on_boarding.*
import kotlinx.android.synthetic.main.activity_on_boarding.view.*
import kotlinx.android.synthetic.main.fragment_item_list.*
import kotlinx.android.synthetic.main.horizontal_rows.view.*
import kotlinx.android.synthetic.main.interest_onboarding.*
import kotlinx.android.synthetic.main.intro_slider.*
import kotlinx.android.synthetic.main.loading_account.*
import kotlinx.android.synthetic.main.onboard_age.*
import kotlinx.android.synthetic.main.onboard_name.*
import kotlinx.android.synthetic.main.onboard_sign_up.*
import kotlinx.android.synthetic.main.onboard_sign_up.view.*
import kotlinx.android.synthetic.main.profile_header.*
import kotlinx.android.synthetic.main.view_image_previewer.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*


class OnBoardingActivity : AppCompatActivity() {


    private var onBoaredAdapter: MyViewPagerAdapter? = null
    private var layoutDots: LinearLayout? = null
    private var layouts: IntArray? = null
    private lateinit var staggeredLayoutManager: StaggeredGridLayoutManager
    private lateinit var adapterInterest: BindingHorizontal
    private var currentUser:FirebaseUser? = null

    private val onItemClickListenerHorizontal = object : BindingHorizontal.OnItemClickListener {
        override fun onItemClick(view: View, position: Int) {
            val text = view.placeNameH
            val top = view.top_blur
            top.alpha = 0.0f
            top.visibility = View.VISIBLE
            top.bringToFront()
            ViewPropertyObjectAnimator.animate(top).alpha(.80f)
                .setDuration(250).start()





            text.bringToFront()

           println("======= binding is clicked")

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentUser = FirebaseAuth.getInstance().currentUser

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        //Set the layout
        setContentView(R.layout.intro_slider)

        layoutDots = findViewById<LinearLayout>(R.id.layoutDots)
        // layouts of all intro sliders
        layouts = intArrayOf(R.layout.onboard_sign_up, R.layout.onboard_name, R.layout.onboard_age, R.layout.interest_onboarding, R.layout.loading_account)

        // adding bottom dots
        addBottomDots(0)

        // making notification bar transparent
        changeStatusBarColor()

        onBoaredAdapter = MyViewPagerAdapter()
        view_pager.adapter = onBoaredAdapter
        view_pager.addOnPageChangeListener(viewPagerPageChangeListener)

        val user_profile = UserInfo()
//



        println("=======current user from on baord ${FirebaseAuth.getInstance().currentUser?.email} and ${intent.getStringExtra("SKIP")}")
        if (intent.getStringExtra("SKIP") == "SKIP"){

            nextPage()
        }



        //println("====== current item ${view_pager.currentItem}")



            //println("========check 1")
            ////controllers for buttons on onboarding screen
            // btn_next.visibility = View.INVISIBLE

        button_done.setOnClickListener {
            println("=====button next")
            it.visibility = View.GONE
            nextPage()
            loading_header.text = "Creating Your Account"

            UserManagement().postProfile(user_profile, currentUser!!.uid, object : UserManagement.PostUserProfile{
                override fun onCallback(bool: Boolean) {

                    GlobalScope.launch {

                        delay(3000)

                        nextPage()
                    }


                }
            })

        }

        btn_nex_intro.setOnClickListener {
            val current = getItem(+1)


            val isConnected = ConnectionUtils().isConnectingToInternet(this)
            if (!isConnected){
                startActivityForResult(Intent(this, NoConnection::class.java), 101)
                return@setOnClickListener
            }


            when(current){

                    1 ->{

                        val user_email = email_onBoard.text
                        val user_passcode = passcode_onBoard.text

                        user_profile.email = user_email.toString()

                        if (TextUtils.isEmpty(user_email)) {
                            email_onBoard.error = "Enter Name"
                            return@setOnClickListener

                        }


                        if (TextUtils.isEmpty(user_passcode)) {
                            passcode_onBoard.error = "Enter Name"
                            return@setOnClickListener

                        }


                        UserManagement().createNewUser(user_profile, passcode_onBoard.text.toString(), this, object : UserManagement.signUpComplete{
                            override fun onCallback(user: FirebaseUser?) {
                                if (user != null) {
                                    currentUser = user
                                    nextPage()
                                }else{
                                    //do something with error
                                }
                            }
                        })

                        //btn_nex_intro.setTextColor(Color.DKGRAY)
                        //btn_nex_intro.text = "Creating Your Account"
                        view_dots.visibility = View.INVISIBLE




                        println("======== user profile ${user_profile.email} and $user_email")
                    }


                    2 ->{
                        val user_name = name_onBoard.text


                        if (TextUtils.isEmpty(user_name)) {
                            name_onBoard.error = "Enter Name"
                            return@setOnClickListener

                        }

                        user_profile.userName = user_name.toString()
                        nextPage()


                        println("======== user profile ${user_profile.userName} and $user_name")
                    }

                    3 ->{
                        val user_age = age_onBoard.text

                        if (TextUtils.isEmpty(user_age)) {
                            age_onBoard.error = "Enter Name"
                            return@setOnClickListener

                        }

                        user_profile.age = user_age.toString()


                        PostUN().downloadCategs(object : PostUN.DownloadCategories{
                            override fun onCallBack(bool: Boolean) {
                                if (bool){
                                    println("======== we have dowbnload categories")
                                    btn_nex_intro.visibility = View.GONE
                                    nextPage()
                                    setUpInterest()
                                    button_done.visibility = View.VISIBLE
                                    button_done.bringToFront()
                                }
                            }
                        })




//                        println("======== user profile ${user_profile.age} and ${user_age.toString()}")
//
//                        loading_header.text = "Creating Your Account"
//                        btn_nex_intro.background = null


                    }


                    5 -> {


//                        btn_nex_intro.background = null

                    }


                }









        }

//
//            btn_nex_intro.setOnClickListener {
//                println("========login")
//
//
//
//
//
//
//
//
////
//                val current = getItem(+1)
//                if (current == layouts!!.size){
//
//                    goToHome()
//
//                }else{
//                    view_pager.currentItem = current
//
//                    view_pager
//                }
//
//                //goToHome()
//            }


//        btn_skip!!.setOnClickListener { goToHome() }
//
//
//
//        btn_next!!.setOnClickListener {
//            // checking for last page & if last page home screen will be launched
//            val current = getItem(+1)
//            if (current < layouts!!.size) {
//                // move to next screen
//                view_pager_onBoarding!!.currentItem = current
//            } else {
//                goToHome()
//            }
//        }







    }

    private fun addBottomDots(currentPage: Int) {
        var dots: Array<TextView?> = arrayOfNulls(layouts!!.size)

        layoutDots!!.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i]!!.text = Html.fromHtml("&#8226;")
            dots[i]!!.textSize = 35f
            dots[i]!!.setTextColor(resources.getIntArray(R.array.inactive_dots)[currentPage])
            layoutDots!!.addView(dots[i])
        }



                ////controllers for buttons on onboarding screen
               // btn_next.visibility = View.INVISIBLE
                //println("========check 3")
//                btn_next.setOnClickListener {
//                    println("========login")
//                    val current = getItem(+1)
//                    view_pager.currentItem = current
//                    //goToHome()
//                }
//
//                sign_up_button.setOnClickListener {
//                    println("========register")
//                    val intent = Intent(applicationContext, RegisterPage::class.java)
//                startActivity(intent)
//                finish()}







        if (dots.isNotEmpty())
            dots[currentPage]!!.setTextColor(resources.getColor(android.R.color.white))
    }

    private fun getItem(i: Int): Int {
        return view_pager!!.currentItem + i
    }



    //	viewpager change listener
    private var viewPagerPageChangeListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageSelected(position: Int) {

            println("========== dots $position and ${layouts?.size}")
            if (position != layouts?.size) addBottomDots(position)

            //println("========check 5")
            //view_pager.currentItem = getItem(+1)
            // changing the next button text 'NEXT' / 'GOT IT'
//            if (position == layouts!!.size - 1) {
//                // last page. make button text to GOT IT
//                btn_next!!.text = getString(R.string.start)
//                btn_skip!!.visibility = View.GONE
//            } else {
//                // still pages are left
//                btn_next!!.text = getString(R.string.next)
//                btn_skip!!.visibility = View.VISIBLE
//            }
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {
        }

        override fun onPageScrollStateChanged(arg0: Int) {
        }


    }

    /**
     * Making notification bar transparent
     */
    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun goToHome() {
        GlobalVals.onBoardingComplete = true
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    /**
     * View pager adapter
     */
    inner class MyViewPagerAdapter : PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = layoutInflater!!.inflate(layouts!![position], container, false)



            container.addView(view)



            return view
        }

        override fun getCount(): Int {
            return layouts!!.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
          //  println("====== hit")

//            val new = view.location_edit.text
//
//            println("==========text inside edit $new")
            return view === obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
//



            container.removeView(view)
        }
    }


    fun nextPage(){
        val current = getItem(+1)
        if (current == layouts!!.size){

            goToHome()

        }else{
            //println("======== from button text ${email_onBoard.text}")
            view_pager.currentItem = current
//
//                    view_pager
        }
    }


    fun setUpInterest() {
        staggeredLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        // val layout = view?.findViewById<RecyclerView>(R.id.list)
        interest_onBoard?.layoutManager = staggeredLayoutManager
        adapterInterest = BindingHorizontal(this, 5)
        interest_onBoard.adapter = adapterInterest
        adapterInterest.setOnItemClickListener(onItemClickListenerHorizontal)
    }
}
