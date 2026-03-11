<template>
	<div>
		<div class="home_box">
			<!-- 食谱信息推荐 -->
			<div class="recomList_view">
<div class="recomList_title"><span>{{tableName}}推荐</span></div>
<div class="categoryList">
	<div class="item" @click="shipuxinxiRecomCategoryChange(-1)" style="cursor: pointer" :class="{active:shipuxinxiRecom_index==-1}">全部</div>
	<div class="item" v-for="(item,index) in shipuxinxiRecomCategorys" @click="shipuxinxiRecomCategoryChange(index)" :class="{active:shipuxinxiRecom_index==index}" style="cursor: pointer">
		{{item}}
	</div>
</div>


<div class="recommendList">
  
  <div class="item" v-for="(item,index) in shipuxinxiRecomList" @click="detailClick('shipuxinxi',item.id)">

    <div class="img-box">
      <img :src="item.imgUrls[0]" alt="">
    </div>
      <div class="content-box">
        <div class="title-box"><div class="title">{{item.shipumingcheng}}</div>
</div>
        <div class="price"></div>
        <div  class="statistic">
          
          <div class="collect">
	<span class="iconfont icon-likeline4"></span>
	<div class="num">{{item.storeupNumber}}</div>
</div>

          <div class="clickNum">
	<span class="iconfont icon-sousuo5"></span>
	<div class="num">{{item.clickNumber}}</div>
</div>

        </div>
        <div class="gengduo"><span>详情</span></div>
      </div>

  </div>

  <div class="recommend_more_view" @click="moreClick('shipuxinxi')">
      <span>查看更多 +</span>
  </div>

</div>

			</div>
			<!-- 食材信息推荐 -->
			<div class="recomList_view">
<div class="recomList_title"><span>{{tableName}}推荐</span></div>
<div class="categoryList">
	<div class="item" @click="shicaixinxiRecomCategoryChange(-1)" style="cursor: pointer" :class="{active:shicaixinxiRecom_index==-1}">全部</div>
	<div class="item" v-for="(item,index) in shicaixinxiRecomCategorys" @click="shicaixinxiRecomCategoryChange(index)" :class="{active:shicaixinxiRecom_index==index}" style="cursor: pointer">
		{{item}}
	</div>
</div>


<div class="recommendList">
  
  <div class="item" v-for="(item,index) in shicaixinxiRecomList" @click="detailClick('shicaixinxi',item.id)">

    <div class="img-box">
      <img :src="item.imgUrls[0]" alt="">
    </div>
      <div class="content-box">
        <div class="title-box"><div class="title">{{item.shicaimingcheng}}</div>
</div>
        <div class="price"></div>
        <div  class="statistic">
          <div class="like">
	<span class="iconfont icon-thumb-up-line1"></span>
	<div class="num">{{item.thumbsupNumber}}</div>
</div>

          <div class="collect">
	<span class="iconfont icon-likeline4"></span>
	<div class="num">{{item.storeupNumber}}</div>
</div>

          <div class="clickNum">
	<span class="iconfont icon-sousuo5"></span>
	<div class="num">{{item.clickNumber}}</div>
</div>

        </div>
        <div class="gengduo"><span>详情</span></div>
      </div>

  </div>

  <div class="recommend_more_view" @click="moreClick('shicaixinxi')">
      <span>查看更多 +</span>
  </div>

</div>

			</div>
			<!-- 食谱安排首页展示 -->
			<div class="homeList_view">
				<div class="homeList_title">
                    <span>食谱安排展示</span>
                </div>
				<div class="homeList">
					<div class="item" v-for="(item,index) in shipuanpaiHomeList" :key="index" @click="detailClick('shipuanpai',item.id)">
						<div class="img-box">
							<img v-if="isHttp(item.fengmian)" :src="item.fengmian.split(',')[0]" alt="">
							<img v-else :src="item.fengmian?$config.url + item.fengmian.split(',')[0]:''" alt="">
						</div>
						<div class="content-box">
							<div class="title">
								{{item.shipumingcheng}}
							</div>
							<div class="statistic">
								<div class="collect">
									<span class="iconfont icon-likeline4"></span>
									<div class="num">{{item.storeupNumber}}</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="homeList_more_view" @click="moreClick('shipuanpai')">
					<span class="homeList_more_text">查看更多 +</span>
				</div>
			</div>
			<!-- 公告信息 -->
			<div class="newsList_view">
				<div class="ntitle"> <div class="n1">公告信息</div> </div>
				
				    <div class="nlist">
				         <ul> 
				        <li v-for="(item,index) in newsList" :key="index" @click="newsDetailClick(item)">
				
				          <div class="imgbox">
				                <img :src="item.imgUrl" >
				          </div>
				          
				          <div class="infobox">
				            <div class="nam">{{item.title}}</div>
				            <div class="info">{{item.introduction}}</div>
				          </div>
				          
				          <div class="tim"><span>{{moment(item.addtime).format('YYYY-MM-DD')}}</span></div>
				          
				        </li>
				         </ul> 
				    </div>
				
				<div class="nmore" @click="moreClick('news')" style="cursor:pointer"><span>查看更多+</span></div>
			</div>

			<div class="appendBox1"></div>
		</div>
		<formModel ref="newsFormModelRef"></formModel>
	</div>
</template>

<script setup>
	import {
		ref,
		getCurrentInstance
	} from 'vue';
	import moment from 'moment'
	import {
		useRouter
	} from 'vue-router';
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	const router = useRouter()
	//食谱信息推荐
	const shipuxinxiRecomCategorys = ref([])
	const shipuxinxiRecom_index = ref(-1)
	const getshipuxinxiRecomCategorys = () => {
		context?.$http({
			url: 'option/shipufenlei/shipufenlei',
			method: 'get'
		}).then(res => {
			shipuxinxiRecomCategorys.value = res.data.data
		})
	}
	getshipuxinxiRecomCategorys()
	const shipuxinxiRecomCategoryChange = (index)=>{
		shipuxinxiRecom_index.value = index
		getshipuxinxiRecomList()
	}
	const shipuxinxiRecomList = ref([])
	const getshipuxinxiRecomList = () => {
		let autoSortUrl = 'shipuxinxi/autoSort'
		if(context?.$toolUtil.storageGet('frontToken')){
			autoSortUrl = "shipuxinxi/autoSort2"
		}
		let params = {
			page: 1,
			limit: 4
		}
		if(shipuxinxiRecom_index.value>=0){
            params.shipufenlei = shipuxinxiRecomCategorys.value[shipuxinxiRecom_index.value]
		}
		context?.$http({
			url: autoSortUrl,
			method: 'get',
			params: params
		}).then(res => {
			shipuxinxiRecomList.value = res.data.data.list
			shipuxinxiRecomList.value.forEach(item=>{
				if(!isHttp(item.shiputupian)){
					item.imgUrls = item.shiputupian.split(',').map(item=>context.$config.url+item)
				}
			})
		})
	}
	//食材信息推荐
	const shicaixinxiRecomCategorys = ref([])
	const shicaixinxiRecom_index = ref(-1)
	const getshicaixinxiRecomCategorys = () => {
		context?.$http({
			url: 'option/shicaileixing/shicaileixing',
			method: 'get'
		}).then(res => {
			shicaixinxiRecomCategorys.value = res.data.data
		})
	}
	getshicaixinxiRecomCategorys()
	const shicaixinxiRecomCategoryChange = (index)=>{
		shicaixinxiRecom_index.value = index
		getshicaixinxiRecomList()
	}
	const shicaixinxiRecomList = ref([])
	const getshicaixinxiRecomList = () => {
		let autoSortUrl = 'shicaixinxi/autoSort'
		if(context?.$toolUtil.storageGet('frontToken')){
			autoSortUrl = "shicaixinxi/autoSort2"
		}
		let params = {
			page: 1,
			limit: 4
		}
		if(shicaixinxiRecom_index.value>=0){
            params.shicaileixing = shicaixinxiRecomCategorys.value[shicaixinxiRecom_index.value]
		}
		context?.$http({
			url: autoSortUrl,
			method: 'get',
			params: params
		}).then(res => {
			shicaixinxiRecomList.value = res.data.data.list
			shicaixinxiRecomList.value.forEach(item=>{
				if(!isHttp(item.shicaitupian)){
					item.imgUrls = item.shicaitupian.split(',').map(item=>context.$config.url+item)
				}
			})
		})
	}
	//食谱安排首页展示
	const shipuanpaiHomeList = ref([])
	const getshipuanpaiHomeList = () => {
		let params = {
			page: 1,
			limit: 4
		}
		context?.$http({
			url: 'shipuanpai/list',
			method: 'get',
			params: params
		}).then(res => {
			shipuanpaiHomeList.value = res.data.data.list
			shipuanpaiHomeList.value.forEach(item=>{
				if(!isHttp(item.fengmian)){
					item.imgUrls = item.fengmian.split(',').map(item=>context.$config.url+item)
				}
			})
		})
	}
	//公告信息弹窗
	import formModel from './news/formModel'
	const newsFormModelRef = ref(null)
	//公告信息
	const newsList = ref([])
	const getNewsList = () => {
		context?.$http({
			url: 'news/list',
			method: 'get',
			params:{
				page:1,
				limit: 4
			}
		}).then(res=>{
			newsList.value = res.data.data.list
			newsList.value.forEach(item=>{
				if(!isHttp(item.picture)){
					item.imgUrl=context.$config.url+item.picture.split(',')[0]
				}
			})
		})
	}
	const newsDetailClick = (item) => {
		if (item && item.id){
			newsFormModelRef.value.init(item.id)
		}
	}
	//判断图片链接是否带http
	const isHttp = (str) => {
        return str && str.substr(0,4)=='http';
    }
	//跳转详情
	const detailClick = (table,id) => {
		router.push(`/index/${table}Detail?id=${id}`)
	}
	const moreClick = (table) => {
		router.push(`/index/${table}List`)
	}
	const init = () => {
		//食谱信息推荐
		getshipuxinxiRecomList()
		//食材信息推荐
		getshicaixinxiRecomList()
		//食谱安排首页展示
		getshipuanpaiHomeList()
		//公告信息
		getNewsList()
	}
	init()
</script>

<style lang="scss">
	.home_box {
	}

	// 推荐
	.recomList_view {
		.recomList_title {
		}
		// list
		// list
		// animation
		.animation_box {
			transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
			z-index: initial;
		}
		.animation_box:hover {
			transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
			-webkit-perspective: 1000px;
			perspective: 1000px;
			transition: 0.3s;
		}
		.animation_box img {
			transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
			z-index: initial;
		}
		.animation_box img:hover {
			transform: rotate(0deg) scale(1.05) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
			-webkit-perspective: 1000px;
			perspective: 1000px;
			transition: 0.3s;
		}
		// animation
		// 更多
		.recommend_more_view {
			.recommend_more_text {
			}
		}
	}
	// 推荐
	// 新闻资讯
	.newsList_view {

		.newsList_title {
		}
		// list
		.news_list_one {
			display: flex;
			flex-wrap: wrap;
			.news_item {
				box-shadow: 0 4px 6px rgba(0,0,0,.3);
				margin: 0 10px 10px;
				background: #fff;
				display: flex;
				width: calc(33% - 20px);
				align-items: center;
				.news_img_box {
					width: 40%;
					font-size: 0;
					.news_img {
						object-fit: cover;
						width: 100%;
						height: 200px;
					}
				}
				.news_content {
					margin: 0 0 0 20px;
					width: calc(60% - 20px);
					.news_title {
						font-weight: bold;
						font-size: 20px;
					}
					.news_text {
						font-size: 14px;
						line-height: 1.5;
					}
					.news_time {
						color: #999;
						width: 100%;
						text-align: right;
					}
				}
			}
		}
		// list
		// animation
		.animation_box {
			transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
			z-index: initial;
		}
		.animation_box:hover {
			transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
			-webkit-perspective: 1000px;
			perspective: 1000px;
			transition: 0.3s;
		}
		.animation_box img {
			transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
			z-index: initial;
		}
		.animation_box img:hover {
			transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
			-webkit-perspective: 1000px;
			perspective: 1000px;
			transition: 0.3s;
		}
		// animation
		// 更多
		.news_more_view {
			.news_more_text {
			}
			.el-icon {
			}
		}
	}
	// 新闻资讯
	// 首页展示
	.homeList_view {

		.homeList_title {
		}
		// list
		// list
		// animation
		.animation_box {
			transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
			z-index: initial;
		}
		.animation_box:hover {
			transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
			-webkit-perspective: 1000px;
			perspective: 1000px;
			transition: 0.3s;
		}
		.animation_box img {
			transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
			z-index: initial;
		}
		.animation_box img:hover {
			transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
			-webkit-perspective: 1000px;
			perspective: 1000px;
			transition: 0.3s;
		}
		// animation
		// 更多
		.homeList_more_view {
			cursor: pointer;
			.homeList_more_text {
			}
		}
	}
	// 首页展示
</style>
<style>
.home_box{
    width: 100%;
    margin: 0px auto;
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
}
.home_box .appendBox1{
    width: 100%;
    height:0px;
    background:#f6f6f6;
    order:5;
}
/* 总盒子 */
.recomList_view{
    width: 1200px;
    margin:20px auto;
    padding: 0;
    background:#fff;
    border-width: 0px;
    border-style: solid;
    border-color:var(--theme2-color50);
    position: relative;
    order: 1;
    display: block;
    box-shadow: 0 2px 4px 0 rgba(0,0,0,.08);
}

/* 标题 */
.recomList_view .recomList_title{
    width: 100%;
    margin: 0px 0 20px;
    z-index:999;
    text-align:left;
    background:none;
    margin-left: -10px;
    border-bottom: 1px #e5e5e5 solid;
    position: relative;
}
.recomList_view .recomList_title span{
    display:inline-block;
    font-size:20px;
    background:var(--theme);
    color:#fff;
    font-weight:500;
    padding:15px 20px 15px 20px;
    min-width:250px;
}
.recomList_view .recomList_title:after {
    content: "";
    height: 1px;
    width: 1px;
    border-style: solid;
    border-width: 5px;
    position: absolute;
    bottom: -10px;
    left: 0;
    border-color: #222 #222 transparent transparent;
}

/* 分类 */
.recomList_view .categoryList{
    display: none;
    flex-wrap: wrap;
}
.recomList_view .categoryList .item{
}

/* 样式七 总盒子 */
.recomList_view .recommendList{
    display: flex;
    flex-wrap: wrap;
    margin: 20px auto 0;
}
.recomList_view .recommendList .item{
    width: calc(25% - 20px);
    margin: 0px 20px 20px 0;
    background: none;
    padding:10px;
    cursor: pointer;
    display:block;
    flex-wrap:wrap;
    justify-content:space-between;
    position:relative;
}

.recomList_view .recommendList .item:nth-child(4n){
    margin-right:0px;
} 

.recomList_view .recommendList .item .img-box{
    width: 100%;
    height: 240px;
    overflow: hidden;
    margin: 0px 0px 5px;
}
.recomList_view .recommendList .item .img-box img{
    width: 100%;
    height: 100%;
    object-fit: cover;
}
.recomList_view .recommendList .item .content-box{
    width: calc(100% - 0px);
    background: none;
    display:flex;
    flex-wrap:wrap;
    align-items:center;
    align-content: center;
    padding:0 0 20px;
}

.recomList_view .recommendList .item .content-box .title-box{
    width: 100%;
    margin: 5px 0;
}
.recomList_view .recommendList .item .content-box .title-box .title{
    width: 100%;
    line-height: 24px;
    color:#333;
    white-space:nowrap;
    overflow:hidden;
    text-overflow:ellipsis;
}
.recomList_view .recommendList .item .content-box .price{
    width: 100%;
    text-align:left;
    color: #f00;
    font-size:16px;
    margin: 5px 0;
}
.recomList_view .recommendList .item .content-box .statistic{
    width: 100%;
    display: flex;
    align-items: center;
    margin:10px 0 0;
    font-size: 14px;
    color: #999;
}
.recomList_view .recommendList .item .content-box .statistic .thumbsupnum{
    display: flex;
    align-items: center;
    margin:0 10px 0 0;
}
.recomList_view .recommendList .item .content-box .statistic .storeupnum{
    display: flex;
    align-items: center;
    margin:0 10px 0 0;
}
.recomList_view .recommendList .item .content-box .statistic .collect{
    display: flex;
    align-items: center;
    margin:0 10px 0 0;
}
.recomList_view .recommendList .item .content-box .statistic .clicknum{
    display: flex;
    align-items: center;
    margin:0 10px 0 0;
}

.recomList_view .recommendList .item .content-box .statistic .iconfont{
    color: inherit;
    margin: 0px 4px 0px 0px;
    display: inline-block;
}
.recomList_view .recommendList .item .content-box .statistic .num{
    color: inherit;
    display: inline-block;
}
.recomList_view .recommendList .item .content-box .statistic .like{
    display: flex;
    align-items: center;
    color: rgb(153, 153, 153);
    font-size: 16px;
    margin: 0px 10px 0px 0px;
}
.recomList_view .recommendList .item .gengduo{
    width: 100%;
    margin: 10px 0 0 0;
    font-size: 15px;
    text-align:right;
    padding-right:10px;
    transition: all 0.3s ease-in-out;
    position:absolute;
    right:10px;
    bottom:20px;
}
.recomList_view .recommendList .item .gengduo span{
    font-size: 16px;
    background:var(--theme);
    color: #fff;
    padding:5px 10px;
    border-radius:20px;
}
.recomList_view .recommendList .item:hover .gengduo{
    display:block;
    color: var(--theme);
    padding-right:0px;
}
.recomList_view .recommendList .item:hover .gengduo span{
}

/* 更多 */
.recomList_view .recommend_more_view{
    width: auto;
    margin: 20px auto;
    z-index:999;
    text-align:center;
    cursor:pointer;
    position:absolute;
    right:20px;
    top:0px;
}
.recomList_view .recommend_more_view span{
    display: inline-block;
    background:none;
    padding:0px 10px;
    font-size: 14px;
    color:#333;
}

/* 总盒子 */
.homeList_view{
    width: 1200px;
    margin:20px auto;
    padding: 0;
    background:#fff;
    border-width: 0px;
    border-style: solid;
    border-color:var(--theme2-color50);
    box-shadow: 0 2px 4px 0 rgba(0,0,0,.08);
    position:relative;
    order: 2;
    display:flex;
    flex-wrap:wrap;
    justify-content:space-between;
    align-items: flex-start;
    align-content: flex-start;
}

/* 标题 */
.homeList_view .homeList_title{
    width: 100%;
    margin: 0px 0 20px;
    z-index:999;
    text-align:left;
    background:none;
    margin-left: -10px;
    border-bottom: 1px #e5e5e5 solid;
    position: relative;
}
.homeList_view .homeList_title span{
    display:inline-block;
    font-size:20px;
    background:var(--theme);
    color:#fff;
    font-weight:500;
    padding:15px 20px 15px 20px;
    min-width:250px;
}
.homeList_view .homeList_title:after {
    content: "";
    height: 1px;
    width: 1px;
    border-style: solid;
    border-width: 5px;
    position: absolute;
    bottom: -10px;
    left: 0;
    border-color: #222 #222 transparent transparent;
}

/* 分类 */
.homeList_view .categoryList{
    width: 180px;
    background: none;
    display: block;
    flex-wrap: wrap;
    margin-right:20px;
    order:1;
    border:0;
    border: 1px solid #eee;
    border-left:none;    
}
.homeList_view .categoryList .item{
    background: none;
    line-height:60px;
    color:#333;
    text-align: center;
    margin:0 0 0px;
    cursor:pointer;
    font-size: 15px;
    border-bottom: 1px solid #eee;
}
.homeList_view .categoryList .item:last-child{
    border-bottom:none;
}
.homeList_view .categoryList .item:hover{
    color:var(--theme);
}
.homeList_view .categoryList .item.active{
    color:var(--theme);
}

/* 样式七 总盒子 */
.homeList_view .homeList{
    width: calc(100% - 200px);
    margin: 0;
    padding:0 20px 0 0;
    display: flex;
    flex-wrap: wrap;
    justify-content:flex-start;
    order:2;
    flex:1;
}
.homeList_view .homeList .item{
    width: calc(50% - 20px);
    margin: 0px 0px 20px 20px;
    padding:20px;
    background: none;
    cursor: pointer;
    overflow:hidden;
    position:relative;
    display:flex;
    flex-wrap:wrap;
    box-shadow: inset 0 0 0 1px #eee;
    justify-content:space-between;
}
.homeList_view .homeList .item:hover{
     box-shadow: inset 0 0 0 1px var(--theme);  
}

.homeList_view .homeList .item .img-box{
    width: 200px;
    height: 168px;
    overflow: hidden;
    margin: 0px;
    text-align:center;
}
.homeList_view .homeList .item .img-box img{
    width: 100%;
    height: 100%;
    object-fit: cover;
}
.homeList_view .homeList .item .content-box{
    width: calc(100% - 220px);
}

.homeList_view .homeList .item .content-box .title{
    width: 100%;
    line-height: 24px;
    white-space:nowrap;
    overflow:hidden;
    text-overflow:ellipsis;
}
.homeList_view .homeList .item:hover .content-box .title{
    color: #000;
}
.homeList_view .homeList .item .content-box .price{
    width: 100%;
    text-align:left;
    color: #f00;
    font-size:16px;
}
.homeList_view .homeList .item:hover .content-box .price{
}
.homeList_view .homeList .item .content-box .statistic{
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: flex-start;
    margin:10px 0 0;
}
.homeList_view .homeList .item:hover .content-box .statistic{
    color: #000;
}
.homeList_view .homeList .item .content-box .statistic .iconfont{
    color: inherit;
    margin: 0px 4px 0px 0px;
}
.homeList_view .homeList .item .content-box .statistic .num{
    color: inherit;
}
.homeList_view .homeList .item .content-box .statistic .like{
    display: flex;
    align-items: center;
    color: inherit;
    font-size: 16px;
    margin: 0px 10px 0px 0px;
}
.homeList_view .homeList .item .content-box .statistic .collect{
    display: flex;
    align-items: center;
    color: inherit;
    font-size: 16px;
    margin: 0px 10px 0px 0px;
}
.homeList_view .homeList .item .content-box .statistic .clickNum{
    display: flex;
    align-items: center;
    color: inherit;
    font-size: 16px;
    margin: 0px 10px 0px 0px;
}

/* 查看更多 */
.homeList_view .homeList_more_view{
    width: auto;
    margin: 20px auto;
    z-index:999;
    text-align:center;
    cursor:pointer;
    position:absolute;
    right:20px;
    top:0px;
}
.homeList_view .homeList_more_view span{
    display: inline-block;
    background:none;
    padding:0px 10px;
    font-size: 14px;
    color:#333;
}
/* 总盒子 */
.newsList_view {
    width: 1200px;
    margin:20px auto;
    padding: 0;
    background:#fff;
    border-width: 0px;
    border-style: solid;
    border-color:var(--theme2-color50);
    box-shadow: 0 2px 4px 0 rgba(0,0,0,.08);
    position:relative;
    order: 6;
    display:block;
    text-align:center;
}

/* 自定义 start*/

/* 标题 */
.newsList_view .ntitle{
    width: 100%;
    margin: 0px 0 20px;
    z-index:999;
    text-align:left;
    background:none;
    margin-left: -10px;
    border-bottom: 1px #e5e5e5 solid;
    position: relative;
}
.newsList_view .ntitle .n1{
    display:inline-block;
    font-size:20px;
    background:var(--theme);
    color:#fff;
    font-weight:500;
    padding:15px 20px 15px 20px;
    min-width:250px;
}
.newsList_view .ntitle:after {
    content: "";
    height: 1px;
    width: 1px;
    border-style: solid;
    border-width: 5px;
    position: absolute;
    bottom: -10px;
    left: 0;
    border-color: #222 #222 transparent transparent;
}

.newsList_view .nlist{
    width: 100%;
    margin:20px auto 0px;
}
.newsList_view .nlist ul{
    margin:0;
    padding:0;
    display: flex;
    flex-wrap: wrap;
    align-items: center;
}
.newsList_view .nlist ul li{
    width: 23.5%;
    text-align: left;
    border: 0px solid #fff;
    cursor:pointer;
    margin:0 2% 20px 0;
    background:#fff;
    padding: 10px 20px;
    overflow: hidden;
    background-repeat: no-repeat;
    background-position: bottom;
    background-image: -webkit-linear-gradient(left, var(--theme), var(--theme));
    background-image: linear-gradient(to left, var(--theme), var(--theme));
    background-size: 100% 0;
    transition: all .5s;
}
.newsList_view .nlist ul li:hover{
}
.newsList_view .nlist ul li:nth-child(4n){
    margin-right:0px;
} 
.newsList_view .nlist ul li:last-child{
}
.newsList_view .nlist ul li .imgbox{
    display:block;
    width: 100%;
    height: 150px;
}
.newsList_view .nlist ul li .imgbox img{
    width: 100%;
    height: 100%;
    object-fit: cover;
    border-radius:0;
}

.newsList_view .nlist ul li .infobox {
    width: calc(100% - 0px);
}
.newsList_view .nlist ul li .infobox .nam {
    width: 100%;
    line-height: 40px;
    color: #333;
    font-size: 15px;
    font-weight: 600;
}
.newsList_view .nlist ul li:hover .infobox .nam{
}
.newsList_view .nlist ul li .infobox .info{
    width: 100%;
    color: #666;
    font-size: 14px;
    line-height: 24px;
    height:72px;
    overflow:hidden;
}
.newsList_view .nlist ul li .tim{
    width: 100%;
    line-height: 40px;
    color: #999;
    font-size: 15px;
    text-align:right;
}
.newsList_view .nlist ul li:hover .tim{
}

/* 更多 */
.newsList_view .nmore{
    width: auto;
    margin: 20px auto;
    z-index:999;
    text-align:center;
    cursor:pointer;
    position:absolute;
    right:20px;
    top:0px;
}
.newsList_view .nmore span{
    display: inline-block;
    background:none;
    padding:0px 10px;
    font-size: 14px;
    color:#333;
}
/* 自定义 end*/

</style>