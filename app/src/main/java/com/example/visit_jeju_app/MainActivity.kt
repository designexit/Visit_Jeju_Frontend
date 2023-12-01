package com.example.visit_jeju_app


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.example.visit_jeju_app.chat.ChatMainActivity
import com.example.visit_jeju_app.community.activity.CommReadActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.visit_jeju_app.MyApplication.Companion.lat
import com.example.visit_jeju_app.MyApplication.Companion.lnt
import com.example.visit_jeju_app.accommodation.AccomActivity
import com.example.visit_jeju_app.accommodation.adapter.AccomAdapter_Main
import com.example.visit_jeju_app.accommodation.model.AccomList
import com.example.visit_jeju_app.chat.ChatActivity
import com.example.visit_jeju_app.databinding.ActivityMainBinding
import com.example.visit_jeju_app.festival.FesActivity
import com.example.visit_jeju_app.festival.adapter.FesAdapter_Main
import com.example.visit_jeju_app.festival.model.FesList
import com.example.visit_jeju_app.gpt.GptActivity
import com.example.visit_jeju_app.login.AuthActivity
import com.example.visit_jeju_app.main.adapter.ImageSliderAdapter
import com.example.visit_jeju_app.main.adapter.RecyclerView
import com.example.visit_jeju_app.restaurant.ResActivity
import com.example.visit_jeju_app.restaurant.adapter.ResAdapter_Main
import com.example.visit_jeju_app.restaurant.model.ResList
import com.example.visit_jeju_app.shopping.ShopActivity
import com.example.visit_jeju_app.shopping.adapter.ShopAdapter_Main
import com.example.visit_jeju_app.shopping.model.ShopList
import com.example.visit_jeju_app.tour.TourActivity
import com.example.visit_jeju_app.tour.adapter.TourAdapter_Main
import com.example.visit_jeju_app.retrofit.NetworkServiceRegionNm
import com.example.visit_jeju_app.tour.adapter.TourAdapter
import com.example.visit_jeju_app.tour.model.TourList
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    // 위치 정보 받아오기 위한 변수 선언 -------------------------------------
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var handler: Handler
    private var lastUpdateTimestamp = 0L
    private val updateDelayMillis = 10000
    // -----------------------------------------------------------

    lateinit var binding: ActivityMainBinding

    //액션버튼 토글
    lateinit var toggle: ActionBarDrawerToggle

    // 메인 비주얼
    lateinit var viewPager_mainVisual: ViewPager2

    // 현재 위치 담아 두는 변수
     var lat : Double = 0.0
     var lnt : Double = 0.0

    // 통신으로 받아온 투어 정보 담는 리스트 , 전역으로 설정, 각 어느 곳에서든 사용가능.
    // 제주 숙박
    lateinit var dataListFromAccomActivity: MutableList<AccomList>
    // 제주 맛집
    lateinit var dataListFromResActivity: MutableList<ResList>
    // 제주 투어
    lateinit var dataListFromTourActivity: MutableList<TourList>
    // 제주 축제
    lateinit var dataListFromFesActivity: MutableList<FesList>
    // 제주 쇼핑
    lateinit var dataListFromShopActivity: MutableList<ShopList>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handler = Handler(Looper.getMainLooper())

        // 위치 받아오기 위해 추가 ---------------------------------------------------------
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        createLocationRequest()
        createLocationCallback()
        //---------------------------------------------------------
        // 각 가테고리별 넘어온 데이터 담을 리스트 초기화, 할당.
        dataListFromAccomActivity = mutableListOf<AccomList>()
        dataListFromResActivity = mutableListOf<ResList>()
        dataListFromTourActivity = mutableListOf<TourList>()
        dataListFromFesActivity = mutableListOf<FesList>()
        dataListFromShopActivity = mutableListOf<ShopList>()

        val headerView = binding.mainDrawerView.getHeaderView(0)
        val headerUserEmail = headerView.findViewById<TextView>(R.id.headerUserEmail)
        val headerLogoutBtn = headerView.findViewById<Button>(R.id.headerLogoutBtn)

        headerLogoutBtn.setOnClickListener {
            // 로그아웃 로직
            MyApplication.auth.signOut()
            MyApplication.email = null
            // 로그아웃 후 처리 (예: 로그인 화면으로 이동)
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }

        val userEmail = intent.getStringExtra("USER_EMAIL") ?: "No Email"
        headerUserEmail.text = userEmail


        setSupportActionBar(binding.toolbar)

        //드로워화면 액션버튼 클릭 시 드로워 화면 나오게 하기
        toggle =
            ActionBarDrawerToggle(
                this@MainActivity,
                binding.drawerLayout,
                R.string.open,
                R.string.close
            )

        binding.drawerLayout.addDrawerListener(toggle)
        //화면 적용하기
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //버튼 클릭스 동기화 : 드로워 열어주기
        toggle.syncState()

        // NavigationView 메뉴 아이템 클릭 리스너 설정
        binding.mainDrawerView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.accommodation -> {
                    startActivity(Intent(this, AccomActivity::class.java))
                    true
                }

                R.id.restaurant -> {
                    startActivity(Intent(this, ResActivity::class.java))
                    true
                }

                R.id.tour -> {
                    startActivity(Intent(this, TourActivity::class.java))
                    true
                }

                R.id.festival -> {
                    startActivity(Intent(this, FesActivity::class.java))
                    true
                }

                R.id.shopping -> {
                    startActivity(Intent(this, ShopActivity::class.java))
                    true
                }

                R.id.community -> {
                    // '커뮤니티' 메뉴 아이템 클릭 시 CommReadActivity로 이동
                    startActivity(Intent(this, CommReadActivity::class.java))
                    true
                }

                R.id.chatting -> {
                    startActivity(Intent(this, ChatMainActivity::class.java))
                    true
                }

                else -> false
            }
        }


        // 메인 카테고리 더보기 링크
        val moreAccomTextView: TextView = findViewById(R.id.mainItemMoreBtn1)
        val moreRestaurantTextView: TextView = findViewById(R.id.mainItemMoreBtn2)
        val moreTourTextView: TextView = findViewById(R.id.mainItemMoreBtn3)
        val moreFestivalTextView: TextView = findViewById(R.id.mainItemMoreBtn4)
        val moreShoppingTextView: TextView = findViewById(R.id.mainItemMoreBtn5)

        // 각 "더보기" 텍스트 뷰에 클릭 리스너를 추가
        moreAccomTextView.setOnClickListener {
            // 제주 숙박 더보기 클릭 시 수행할 동작
            val intent = Intent(this, AccomActivity::class.java)
            startActivity(intent)
        }

        moreRestaurantTextView.setOnClickListener {
            val intent = Intent(this, ResActivity::class.java)
            startActivity(intent)
        }

        moreTourTextView.setOnClickListener {
            val intent = Intent(this, TourActivity::class.java)
            startActivity(intent)
        }

        moreFestivalTextView.setOnClickListener {
            val intent = Intent(this, FesActivity::class.java)
            startActivity(intent)
        }

        moreShoppingTextView.setOnClickListener {
            val intent = Intent(this, ShopActivity::class.java)
            startActivity(intent)
        }


// ========== 각 카테고리 별 데이터 불러오기 ========== //

        val networkService = (applicationContext as MyApplication).networkService

        // 제주 숙박
        val accomListCall = networkService.GetAccomList()

        accomListCall.enqueue(object : Callback<List<AccomList>> {
            override fun onResponse(
                call: Call<List<AccomList>>,
                accomponse: Response<List<AccomList>>

            ) {
                val accomList = accomponse.body()

                Log.d("ljs", "accomModel 값 : ${accomList}")

                //데이터 받기 확인 후, 리스트에 담기.
                accomList?.get(0)?.let { dataListFromAccomActivity.add(it) }
                accomList?.get(1)?.let { dataListFromAccomActivity.add(it) }
                accomList?.get(2)?.let { dataListFromAccomActivity.add(it) }
                accomList?.get(3)?.let { dataListFromAccomActivity.add(it) }
                accomList?.get(4)?.let { dataListFromAccomActivity.add(it) }
                Log.d(
                    "lsy",
                    "test 값 추가 후 확인 : dataListFromTourActivity 길이 값 : ${dataListFromAccomActivity?.size}"
                )
                val accomLayoutManager =
                    LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                binding.viewRecyclerAccom.layoutManager = accomLayoutManager
                binding.viewRecyclerAccom.adapter =
                    AccomAdapter_Main(this@MainActivity, dataListFromAccomActivity)
            }

            override fun onFailure(call: Call<List<AccomList>>, t: Throwable) {
                Log.d("lsy", "fail")
                call.cancel()
            }
        })

        // 제주 맛집
        val resListCall = networkService.GetResList()

        resListCall.enqueue(object : Callback<List<ResList>> {
            override fun onResponse(
                call: Call<List<ResList>>,
                response: Response<List<ResList>>

            ) {
                val resList = response.body()

                Log.d("ljs", "resModel 값 : ${resList}")

                //데이터 받기 확인 후, 리스트에 담기.
                resList?.get(0)?.let { dataListFromResActivity.add(it) }
                resList?.get(1)?.let { dataListFromResActivity.add(it) }
                resList?.get(2)?.let { dataListFromResActivity.add(it) }
                resList?.get(3)?.let { dataListFromResActivity.add(it) }
                resList?.get(4)?.let { dataListFromResActivity.add(it) }
                Log.d(
                    "lsy",
                    "test 값 추가 후 확인 : dataListFromResActivity 길이 값 : ${dataListFromResActivity?.size}"
                )
                val resLayoutManager =
                    LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                binding.viewRecyclerRestaurant.layoutManager = resLayoutManager
                binding.viewRecyclerRestaurant.adapter =
                    ResAdapter_Main(this@MainActivity, dataListFromResActivity)
            }

            override fun onFailure(call: Call<List<ResList>>, t: Throwable) {
                Log.d("lsy", "fail")
                call.cancel()
            }
        })


        // 제주 투어
        val tourListCall = networkService.GetTourList()

        tourListCall.enqueue(object : Callback<List<TourList>> {
            override fun onResponse(
                call: Call<List<TourList>>,
                response: Response<List<TourList>>

            ) {
                val tourList = response.body()

                Log.d("lsy", "tourModel 값 : ${tourList}")

                //데이터 받기 확인 후, 리스트에 담기.
                tourList?.get(0)?.let { dataListFromTourActivity.add(it) }
                tourList?.get(1)?.let { dataListFromTourActivity.add(it) }
                tourList?.get(2)?.let { dataListFromTourActivity.add(it) }
                tourList?.get(3)?.let { dataListFromTourActivity.add(it) }
                tourList?.get(4)?.let { dataListFromTourActivity.add(it) }
                Log.d(
                    "lsy",
                    "test 값 추가 후 확인 : dataListFromTourActivity 길이 값 : ${dataListFromTourActivity?.size}"
                )
                val tourLayoutManager =
                    LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                binding.viewRecyclerTour.layoutManager = tourLayoutManager
                binding.viewRecyclerTour.adapter =
                    TourAdapter_Main(this@MainActivity, dataListFromTourActivity)
            }

            override fun onFailure(call: Call<List<TourList>>, t: Throwable) {
                Log.d("lsy", "fail")
                call.cancel()
            }
        })

        // 제주 축제
        val fesListCall = networkService.GetFesList()

        fesListCall.enqueue(object : Callback<List<FesList>> {
            override fun onResponse(
                call: Call<List<FesList>>,
                response: Response<List<FesList>>

            ) {
                val fesList = response.body()

                Log.d("ljs", "fesModel 값 : ${fesList}")

                //데이터 받기 확인 후, 리스트에 담기.
                fesList?.get(0)?.let { dataListFromFesActivity.add(it) }
                fesList?.get(1)?.let { dataListFromFesActivity.add(it) }
                fesList?.get(2)?.let { dataListFromFesActivity.add(it) }
                fesList?.get(3)?.let { dataListFromFesActivity.add(it) }
                fesList?.get(4)?.let { dataListFromFesActivity.add(it) }
                Log.d(
                    "lsy",
                    "test 값 추가 후 확인 : dataListFromTourActivity 길이 값 : ${dataListFromFesActivity?.size}"
                )
                val fesLayoutManager =
                    LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                binding.viewRecyclerFestival.layoutManager = fesLayoutManager
                binding.viewRecyclerFestival.adapter =
                    FesAdapter_Main(this@MainActivity, dataListFromFesActivity)
            }

            override fun onFailure(call: Call<List<FesList>>, t: Throwable) {
                Log.d("lsy", "fail")
                call.cancel()
            }
        })

        // 제주 쇼핑
        val shopListCall = networkService.GetShopList()

        shopListCall.enqueue(object : Callback<List<ShopList>> {
            override fun onResponse(
                call: Call<List<ShopList>>,
                response: Response<List<ShopList>>

            ) {
                val shopList = response.body()

                Log.d("ljs", "shopModel 값 : ${shopList}")

                //데이터 받기 확인 후, 리스트에 담기.
                shopList?.get(0)?.let { dataListFromShopActivity.add(it) }
                shopList?.get(1)?.let { dataListFromShopActivity.add(it) }
                shopList?.get(2)?.let { dataListFromShopActivity.add(it) }
                shopList?.get(3)?.let { dataListFromShopActivity.add(it) }
                shopList?.get(4)?.let { dataListFromShopActivity.add(it) }
                Log.d(
                    "lsy",
                    "test 값 추가 후 확인 : dataListFromTourActivity 길이 값 : ${dataListFromShopActivity?.size}"
                )
                val shopLayoutManager =
                    LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                binding.viewRecyclerShopping.layoutManager = shopLayoutManager
                binding.viewRecyclerShopping.adapter =
                    ShopAdapter_Main(this@MainActivity, dataListFromShopActivity)
            }

            override fun onFailure(call: Call<List<ShopList>>, t: Throwable) {
                Log.d("lsy", "fail")
                call.cancel()
            }
        })


        // 메인 비주얼
        viewPager_mainVisual = findViewById(R.id.viewPager_mainVisual)
        viewPager_mainVisual.adapter = ImageSliderAdapter(getMainvisual()) // 어댑터 생성
        viewPager_mainVisual.orientation = ViewPager2.ORIENTATION_HORIZONTAL // 방향을 가로로

        val NUM_PAGES = 4 // 전체 페이지 수
        var currentPage = 0

        // 자동 스크롤을 위한 Handler 생성
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                currentPage = (currentPage + 1) % NUM_PAGES // 다음 페이지로 이동
                viewPager_mainVisual.setCurrentItem(currentPage, true) // 다음 페이지로 슬라이드

                handler.postDelayed(this, 3000) // 3초 후에 다음 페이지로 이동
            }
        }

        // 자동 스크롤 시작
        handler.postDelayed(runnable, 3000) // 3초 후에 첫 번째 페이지로 이동


        // 일단, 시작시 토스트로 현재 위치 위도, 경도 받아오기 테스트
        getLocation {

            Log.d("lsy", "현재 위치 조회 3 : lat : $lat, lnt : $lnt")
        // 현재 위치 백에 보내서, 데이터 받아오기.
        // http://10.100.104.32:8083/tour/tourList/tourByGPS?lat=33.4&lnt=126.2


        val tourGPSCall = networkService.getTourGPS(lat, lnt)

        Log.d("lsy", "현재 위치 조회 4 : lat : $lat, lnt : $lnt")

        tourGPSCall.enqueue(object : Callback<List<TourList>> {
            override fun onResponse(
                call: Call<List<TourList>>,
                response: Response<List<TourList>>

            ) {
                val tourList = response.body()

                Log.d("lsy", "tourList 값 : ${tourList}")
                Log.d("lsy", "tourList 사이즈 : ${tourList?.size}")
            }

            override fun onFailure(call: Call<List<TourList>>, t: Throwable) {
                Log.d("lsy", "fail")
                call.cancel()
            }
        })
    }





        // Bottom Navigation link
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.home -> {
                    // 홈 아이템 클릭 처리
                    val intent = Intent(this@MainActivity, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.chat -> {
                    val intent = Intent(this@MainActivity, GptActivity::class.java)
                    startActivity(intent)
                    true
                }
                /*R.id.youtube -> {
                    val videoUrl = "https://www.youtube.com/c/visitjeju" // 여기에 YouTube 링크를 입력하세요

                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
                    intent.putExtra("force_fullscreen", true) // 전체 화면으로 열고 싶은 경우

                    // YouTube 앱이 없을 경우 대체 앱(웹 브라우저)을 열기
                    val youtubeAppPackage = "com.google.android.youtube"
                    if (isAppInstalled(youtubeAppPackage)) {
                        intent.`package` = youtubeAppPackage
                    } else {
                        intent.setPackage(null)
                    }

                    startActivity(intent)
                    true
                }*/
                R.id.youtube -> {
                    val webpageUrl = "https://www.youtube.com/c/visitjeju" // 웹 페이지 링크를 입력하세요

                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webpageUrl))
                    startActivity(intent)
                    true
                }
                R.id.instagram -> {
                    val webpageUrl = "https://www.instagram.com/visitjeju.kr" // 웹 페이지 링크를 입력하세요

                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webpageUrl))
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        val communityBanner = findViewById<ImageView>(R.id.communityBanner)

        // ImageView를 클릭했을 때 동작하는 이벤트 리스너 추가
        communityBanner.setOnClickListener {
            // 클릭 시 새로운 화면으로 이동하는 Intent 생성
            val intent = Intent(this, CommReadActivity::class.java)
            startActivity(intent) // 새로운 화면으로 이동
        }


    } //onCreate

    @SuppressLint("MissingPermission")
    private fun getLocation(callback: () -> Unit) {
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)

        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { success: Location? ->
                success?.let { location ->
                    Log.d("lsy", "현재 위치 조회 : lat : ${location.latitude}, lnt : ${location.longitude}")
                    lat = location.latitude
                    lnt = location.longitude
                    Log.d("lsy", "현재 위치 조회 2 : lat : ${lat}, lnt : ${lnt}")

                    callback()
                }
            }
            .addOnFailureListener { fail ->
                Log.d("lsy", "현재 위치 조회 실패")
            }
    }

    // 위치 데이터 획득 추가 ---------------------------------------------------------
    private fun createLocationRequest() {
        locationRequest = LocationRequest.create()?.apply {
            interval = 40000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }!!
    }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                for (location in locationResult.locations){
                    // 여기서 위치 정보를 사용하세요.
                    sendLocationToServer(lat, lnt)
                }
            }
        }
    }
    // -----------------------------------------------------------------------------


    // 위치 정보 업데이트 ---------------------------------------------------------------------
    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }
    //-----------------------------------------------------------------------------------------


    // 백엔드 서버로 위치 데이터 전송 -----------------------------------------------------------------------
    private fun sendLocationToServer(lat: Double, lnt: Double) {
    val networkService = (applicationContext as MyApplication).networkService
        val tourGPSCall = networkService.getTourGPS(lat, lnt )

    tourGPSCall.enqueue(object : Callback<List<TourList>> {
            override fun onResponse(call: Call<List<TourList>>, response: Response<List<TourList>>) {
                Log.d("lsy", "현재 위치 업데이트 성공: lat : ${lat}, lnt : ${lnt}")
            }

            override fun onFailure(call: Call<List<TourList>>, t: Throwable) {
                Log.d("lsy", "현재 위치 업데이트 실패: lat : ${lat}, lnt : ${lnt}")
            }
        })
    }

    // 현재 위치 백에 다시 보내서, 데이터 업데이트.
    // http://10.100.104.32:8083/tour/tourList/tourByGPS?lat=33.4&lnt=126.2

//    //--------------------------------------------------------------------------------------------

    // 위치 정보 업데이트 중지 -------------------------------------------------------------------------
    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
    //-------------------------------------------------------------------------------------------


    // 뷰 페이저에 들어갈 아이템
    private fun getMainvisual(): ArrayList<Int> {
        return arrayListOf<Int>(
            R.drawable.jeju_apec02,
            R.drawable.jeju_apec03,
            R.drawable.jeju_apec04,
            R.drawable.jeju_apec01,)
    }




    // 앱 설치 여부 확인 함수
    private fun isAppInstalled(packageName: String): Boolean {
        return try {
            applicationContext.packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    // 웹 페이지 열기 함수
    private fun openWebPage(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)

        // 검색 뷰에, 이벤트 추가하기.
        val menuItem = menu?.findItem(R.id.menu_toolbar_search)
        // menuItem 의 형을 SearchView 타입으로 변환, 형변환
        val searchView = menuItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                //검색어가 변경시 마다, 실행될 로직을 추가.
                Log.d("kmk","텍스트 변경시 마다 호출 : ${newText} ")
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                // 검색어가 제출이 되었을 경우, 연결할 로직.
                // 사용자 디비, 검색을하고, 그 결과 뷰를 출력하는 형태.
                Toast.makeText(this@MainActivity,"검색어가 전송됨 : ${query}", Toast.LENGTH_SHORT).show()
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

}

