package com.example.visit_jeju_app


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.example.visit_jeju_app.chat.ChatMainActivity
import com.example.visit_jeju_app.community.activity.CommReadActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.visit_jeju_app.chat.ChatActivity
import com.example.visit_jeju_app.databinding.ActivityMainBinding
import com.example.visit_jeju_app.main.adapter.ImageSliderAdapter
import com.example.visit_jeju_app.main.adapter.RecyclerView
import com.example.visit_jeju_app.tour.TourActivity
import com.example.visit_jeju_app.tour.adapter.TourAdapter
import com.example.visit_jeju_app.tour.adapter.TourAdapter_Main
import com.example.visit_jeju_app.tour.model.TourList
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    //액션버튼 토글
    lateinit var toggle: ActionBarDrawerToggle

    // 비주얼 슬라이더
    lateinit var viewPager_mainVisual: ViewPager2

    // 통신으로 받아온 투어 정보 담는 리스트 , 전역으로 설정, 각 어느 곳에서든 사용가능.
    lateinit var dataListFromTourActivity: MutableList<TourList>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.toolbar)

        // 투어에서 넘어온 데이터 담을 리스트 초기화, 할당.
        dataListFromTourActivity = mutableListOf<TourList>()

        //드로워화면 액션버튼 클릭 시 드로워 화면 나오게 하기
        toggle =
            ActionBarDrawerToggle(this@MainActivity, binding.drawerLayout,R.string.open, R.string.close)

        binding.drawerLayout.addDrawerListener(toggle)
        //화면 적용하기
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //버튼 클릭스 동기화 : 드로워 열어주기
        toggle.syncState()

        // NavigationView 메뉴 아이템 클릭 리스너 설정
        binding.mainDrawerView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.community -> {
                    // '커뮤니티' 메뉴 아이템 클릭 시 CommReadActivity로 이동
                    startActivity(Intent(this, CommReadActivity::class.java))
                    true
                }
                // 다른 메뉴 아이템에 대한 처리 추가

                else -> false
            }
        }


        // MainActivity에서 TourActivity를 시작하고 TourActivity에서 데이터를 가져오기 위한 부분:
//        val intent = Intent(this@MainActivity, TourActivity::class.java)
//        startActivity(intent)



        // 리사이클러 뷰 붙이기
        val datasHotel = mutableListOf<String>()
        for(i in 1..5) {
            datasHotel.add("제주 숙박 : $i")
        }
        val datasRestaurant = mutableListOf<String>()
        for(i in 1..5) {
            datasRestaurant.add("제주 맛집 : $i")
        }
        val datasTour = mutableListOf<String>()
        for(i in 1..5) {
            datasTour.add("제주 투어 : $i")
        }
        val datasFestival = mutableListOf<String>()
        for(i in 1..5) {
            datasFestival.add("제주 축제 : $i")
        }
        val datasShopping = mutableListOf<String>()
        for(i in 1..5) {
            datasShopping.add("제주 쇼핑 : $i")
        }

        // 제주 숙박
        /*val hotelLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        //val linearLayoutManager = LinearLayoutManager(this)

        // 리사이클러 뷰 속성 옵션에 출력 옵션 붙이기
        binding.viewRecyclerHotel.layoutManager = hotelLayoutManager
        // 리사이클러뷰 속성 옵션에 데이터를 붙이기 , 어댑터 를 연결한다.
        val AccomAdapter = RecyclerView(datasHotel)
        binding.viewRecyclerHotel.adapter = AccomAdapter*/

       // 제주 맛집
        /*val restaurantLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.viewRecyclerRestaurant.layoutManager = restaurantLayoutManager
        val ResAdapter = RecyclerView(datasRestaurant)
        binding.viewRecyclerRestaurant.adapter = ResAdapter*/

        // 제주 투어
//        val tourLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        binding.viewRecyclerTour.layoutManager = tourLayoutManager
        // 더미 데이터 테스트
        val TourAdapter = RecyclerView(datasTour)
//        binding.viewRecyclerTour.adapter = TourAdapter

        // 제주 축제
        /*val festivalLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.viewRecyclerFestival.layoutManager = festivalLayoutManager
        val FesAdapter = RecyclerView(datasFestival)
        binding.viewRecyclerFestival.adapter = FesAdapter*/

        // 제주 쇼핑
        /*val shoppingLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.viewRecyclerShopping.layoutManager = shoppingLayoutManager
        val ShopAdapter = RecyclerView(datasShopping)
        binding.viewRecyclerShopping.adapter = ShopAdapter*/

        // TourActivity에서 MainActivity로 데이터를 전달하는 부분:
        // TourActivity에서 데이터를 가져온 후 MainActivity로 돌아가기 위한 코드
        // TourActivity에서 데이터를 가져오는 코드 뒤에 작성
        // 예시에서는 TourActivity에서 가져온 데이터를 ArrayList로 가정합니다.

//        val dataListFromTourActivity: ArrayList<TourList> = 가져온_데이터_리스트

//         dataListFromTourActivity = null
        // 네트워크 통신 , 데이터를 받아와서, 위에 리스트에 담기.

        val networkService = (applicationContext as MyApplication).networkService
        val tourListCall = networkService.GetTourList()

        tourListCall.enqueue(object : Callback<List<TourList>> {
            override fun onResponse(
                call: Call<List<TourList>>,
                response: Response<List<TourList>>

            ) {
                val tourList = response.body()

                Log.d("lsy","tourModel 값 : ${tourList}")

                //데이터 받기 확인 후, 리스트에 담기.
                // 전체 623개
//                dataListFromTourActivity?.addAll(tourList as Collection<TourList>)
                tourList?.get(0)?.let { dataListFromTourActivity.add(it) }
                tourList?.get(1)?.let { dataListFromTourActivity.add(it) }
                tourList?.get(2)?.let { dataListFromTourActivity.add(it) }
                Log.d("lsy","test 값 추가 후 확 : dataListFromTourActivity 길이 값 : ${dataListFromTourActivity?.size}")
                val tourLayoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                binding.viewRecyclerTour.layoutManager = tourLayoutManager
                binding.viewRecyclerTour.adapter = TourAdapter_Main(this@MainActivity,dataListFromTourActivity)
            }


            override fun onFailure(call: Call<List<TourList>>, t: Throwable) {
                Log.d("lsy", "fail")
                call.cancel()
            }
        })


       // binding.viewRecyclerTour.adapter = TourAdapter_Main(this@MainActivity,dataListFromTourActivity)

        Log.d("lsy","dataListFromTourActivity 길이 값 : ${dataListFromTourActivity?.size}")

        // MainActivity로 데이터 전달
//        val intent = Intent()
//        intent.putParcelableArrayListExtra("TOUR_DATA", dataListFromTourActivity)
//        setResult(Activity.RESULT_OK, intent)
//        finish() // TourActivity 종료



       /* // 메인 슬라이더
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)

        // 뷰페이저2 어댑터 설정하기
        val images = listOf(R.drawable.jeju_apec01, R.drawable.jeju_apec02, R.drawable.jeju_apec03, R.drawable.jeju_apec04)
        val imageSliderAdapter = ImageSliderAdapter(images)
        viewPager.adapter = imageSliderAdapter*/

        viewPager_mainVisual = findViewById(R.id.viewPager_mainVisual)
        viewPager_mainVisual.adapter = ImageSliderAdapter(getMainvisual()) // 어댑터 생성
        viewPager_mainVisual.orientation = ViewPager2.ORIENTATION_HORIZONTAL // 방향을 가로로





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
                    val intent = Intent(this@MainActivity, ChatActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.youtube -> {
                    openWebPage("https://www.youtube.com/c/visitjeju")
                    true
                }
                R.id.instagram -> {
                    openWebPage("https://www.instagram.com/visitjeju.kr")
                    true
                }
                else -> false
            }
        }


    } //onCreate

    // 메인 슬라이더 : 뷰 페이저에 들어갈 아이템
    private fun getMainvisual(): ArrayList<Int> {
        return arrayListOf<Int>(
            R.drawable.jeju_apec01,
            R.drawable.jeju_apec02,
            R.drawable.jeju_apec03,
            R.drawable.jeju_apec04)
    }

    private fun openWebPage(url: String) {
        try {
            val webpage = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, webpage)
            val packageManager = applicationContext.packageManager
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this@MainActivity, "No app can handle this link", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            // Handle exceptions, like malformed URLs
            Toast.makeText(this@MainActivity, "Error opening web page", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
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

