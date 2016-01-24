# Git使用文档

## 建立项目

1. 新建项目
		
		进入http://pdktest.lifecreatech.com:9000/
		用自己的账号登录
		点击右上角的 加号（+）新建项目
		填写项目名称
		选择组为 lifeCreate
		“Visibility Level”是项目权限，可以根据项目需要自行选择
		点击 Creat Project
		
2. 完善结构
		
		按照页面提示的命令在终端操作即可
		
``` git
		git config --global user.name "your name"
		git config --global user.email "your email"
		
		mkdir test
		cd test
		git init
		touch README
		git add README
		git commit -m 'first commit'
		git remote add git@pdktest.lifecreatech.com:lifecreate/workdoc.git
		
		git push -u origin master
		
``` 
		
		
				
3. 添加ssh key

		点击右上角的 人形 图标
		选择上面导航栏的 SSH Keys
		点击右上角的 Add SSH Keys 进入下一页
		点击 the SSH help page 进入帮助页面
		点击左侧的 SSH 即可
		
		按照页面提示操作，在终端输入以下命令
		
``` ssh
		ssh-keygen -t rsa -C "$your_email"
		cat ~/.ssh/id_rsa.pub
		
```
		将显示的内容粘贴到 添加SSH Key的页面
		添加SSH Key就是建立本机和gitlab之间的信任关系，以便以后在获取代码和提交代码时不用输入
		用户名和密码


## 开发流程

1. 获取代码

		git clone git@pdktest.lifecreatech.com:lifecreate/workdoc.git	
	
2. 提交代码

		git status   //先查看一下状态
		git commit -a -m "提交的说明" //提交到本地
		git push //提交到远程服务器
		
		注意：第一次提交的时候，最好提交.gitignore文件
	
3. 更新代码

		git fetch
		git merge origin/master  (或者是对应的分支)
		
		注意：尽量不要使用 git pull
		
4. 回滚代码

		git log  // 找到要回滚的版本
		git reset --hard 版本号   // 回滚到指定版本
		
		有关git log 推荐使用：
		git log --graph --pretty=format:'%Cred%h%Creset -%C(yellow)%d%Creset %s     %Cgreen(%cr)%Creset' --abbrev-commit --date=relative

5. 处理冲突

		git 发生冲突的情况比较少见，一旦出现可以根据不同的类型查询帮助文档即可


## 上线流程

1. tag相关

		//创建版本号为1.0.0的版本
		git tag -a v1.0.0 -m “标签的描述”
		//显示tag列表
		git tag -l
		//删除指定标签
		git tag -d v1.0.0 
		//将指定标签提交到git服务器
		git push origin v1.0.0

2. 分支相关
	
		//创建分支
		git branch name
		//切换分支
		git checkout name
		//创建+切换分支
		git checkout -b name
		//合并某分支到当前分支
		git merge name
		//删除分支
		git branch -d name
		//删除远程分支
		git push origin : name
		//查看分支
		git branch
		
3. 开发流程

		默认会创建master分支
		完成第一次上线后，在master上打tag
		然后根据tag创建develop分支
		常规开发都在develop上进行
		每次上线之后需要将代码合并到master上，然后打tag
		
		如果在两次常规上线之间需要修复bug并且上线
		可以根据master上一个tag创建一个fixbug分支
		上线fixbug分支后，需要合并到master上并且打tag
		同时需要合并到develop分支上
		然后根据需要可以选择删除fixbug分支