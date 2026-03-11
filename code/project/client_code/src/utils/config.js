const config = {
    get() {
        return {
            url : process.env.VUE_APP_BASE_API_URL + process.env.VUE_APP_BASE_API + '/',
            name: process.env.VUE_APP_BASE_API,
			menuList:[
				{
					name: '菜谱信息',
					icon: 'icon-common15',
					child:[

						{
							name:'食谱信息',
							url:'/index/shipuxinxiList'
						},
					]
				},
				{
					name: '食材信息管理',
					icon: '',
					child:[

						{
							name:'食材信息',
							url:'/index/shicaixinxiList'
						},
					]
				},
				{
					name: '食谱安排管理',
					icon: '',
					child:[

						{
							name:'食谱安排',
							url:'/index/shipuanpaiList'
						},
					]
				},
				{
					name: '论坛交流',
					icon: 'icon-common46',
					child:[

						{
							name:'美食论坛',
							url:'/index/forumList'
						},
					]
				},
				{
					name: '新闻资讯管理',
					icon: '',
					child:[

						{
							name:'公告信息',
							url:'/index/newsList'
						},
					]
				},
			]
        }
    },
    getProjectName(){
        return {
            projectName: "美食推荐分享系统"
        } 
    }
}
export default config
