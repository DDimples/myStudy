title: Spring web项目中常用的annotation (一)
date: 2015-12-23 13:18:02
tags: Spring 

---


# Spring web项目中常用的annotation

在使用Spring框架开发web项目的过程中，使用注解可是实现代码的松耦合，灵活方便的进行web开发。这篇文章将详细介绍开发过程中用到的一些annotation及加载原理。


<!--more-->

## Component、Service、Controller和Repository

这些注解的目的是为了将类标识为Bean，spring基于注解的扫描只需在application.xml文件中添加如下配置：

```
<context:component-scan/>
```

* Component： Service、Controller和Repository三个注解都加上了Component注解的标记。Component是一个泛化的概念，仅仅表示一个组件 (Bean) ，可以作用在任何层次，被标记了该注解的类，在系统启动是都会被扫描；
* Service：通常作用在业务层；
* Controller：通常作用在控制层；
* Repository：通常作用在dao层；

### Spring如何扫描这些注解并注册bean的

**ComponentScanBeanDefinitionParser**这个类就是用来将<context:component-scan/>标签转化为bean的解析类，具体代码由于如下：

```
@Override
public BeanDefinition parse(Element element, ParserContext parserContext) {
	//从配置文件中获取base-package的值
    String basePackage = element.getAttribute(BASE_PACKAGE_ATTRIBUTE);
    basePackage = parserContext.getReaderContext().getEnvironment().resolvePlaceholders(basePackage);
    String[] basePackages = StringUtils.tokenizeToStringArray(basePackage,
            ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);

    // Actually scan for bean definitions and register them.
    ClassPathBeanDefinitionScanner scanner = configureScanner(parserContext, element);
    //扫描并选出符合条件的bean
    Set<BeanDefinitionHolder> beanDefinitions = scanner.doScan(basePackages);
    //注册符合条件的bean
    registerComponents(parserContext.getReaderContext(), beanDefinitions, element);

    return null;
}
```

#### 如何扫描annotation

首先看看是如何扫描符合条件的bean的，scanner.doScan()方法中findCandidateComponents()方法首先获取base-package路径下的资源Resource，然后判断资源是否可读，并且获取可读资源的MetadataReader对象，然后再调用isCandidateComponent(MetadataReader)判段是否是候选组件，如果是，则生成该metadataReader的ScannedGenericBeanDefinition对象。最后判断ScannedGenericBeanDefinition是否为候选的，如果是则添加到工厂中。

```
protected boolean isCandidateComponent(MetadataReader metadataReader) throws IOException {
	
    for (TypeFilter tf : this.excludeFilters) {
        if (tf.match(metadataReader, this.metadataReaderFactory)) {
            return false;
        }
    }
    for (TypeFilter tf : this.includeFilters) {
        if (tf.match(metadataReader, this.metadataReaderFactory)) {
            return isConditionMatch(metadataReader);
        }
    }
    return false;
}
```
上面的excludeFilters和includeFilters是两个LinkedList，excludeFilters默认为null，includeFilters默认包含org.springframework.stereotype.Component和javax.annotation.ManagedBean两个注解。

至此已经很明了了，如果我们想要扫描自己定义的注解，或者不希望容器扫描哪些注解，也只需要添加配置即可，如：

```
<context:component-scan base-package="com.mystudy.web">
       <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Repository"/>
       <context:include-filter type="annotation" expression="some other annotation"/>
</context:component-scan>
```

#### 如何注册bean

回到上面的scanner.doScan()方法，查找到相应bean后，循环处理并注册到beanDefinitionMap中，该map为ConcurrentHashMap：

```
protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
    Assert.notEmpty(basePackages, "At least one base package must be specified");
    Set<BeanDefinitionHolder> beanDefinitions = new LinkedHashSet<BeanDefinitionHolder>();
    for (String basePackage : basePackages) {
    	//查找符合条件的bean
        Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
        for (BeanDefinition candidate : candidates) {
            ScopeMetadata scopeMetadata = this.scopeMetadataResolver.resolveScopeMetadata(candidate);
            //添加Scope，默认为singleton
            candidate.setScope(scopeMetadata.getScopeName());
            //根据类名生成beanName
            String beanName = this.beanNameGenerator.generateBeanName(candidate, this.registry);
            if (candidate instanceof AbstractBeanDefinition) {
                postProcessBeanDefinition((AbstractBeanDefinition) candidate, beanName);
            }
            if (candidate instanceof AnnotatedBeanDefinition) {
                AnnotationConfigUtils.processCommonDefinitionAnnotations((AnnotatedBeanDefinition) candidate);
            }
            if (checkCandidate(beanName, candidate)) {
                BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(candidate, beanName);
                definitionHolder = AnnotationConfigUtils.applyScopedProxyMode(scopeMetadata, definitionHolder, this.registry);
                beanDefinitions.add(definitionHolder);
                //将bean注册到registry，该处代码就不再贴了~ 
                registerBeanDefinition(definitionHolder, this.registry);
            }
        }
    }
    return beanDefinitions;
}
```

this.registry是一个BeanDefinitionRegistry，最终执行的为DefaultListableBeanFactory。

到这我们知道了我们的bean是怎样被注册管理的了。但是问题又来了，我们的系统是在什么时候读取<context:component-scan/>标签，并执行后续操作的呢？

#### ContextLoader

在web.xml中，我们会配置这样一个listener，一切都是从这里开始的。

```
<listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
```

在ContextLoaderListener中contextInitialized()会再初始化的时候被调用：

```
@Override
public void contextInitialized(ServletContextEvent event) {
    initWebApplicationContext(event.getServletContext());
}
```

initWebApplicationContext最终来到这个父类的这个地方：

```
protected Class<?> determineContextClass(ServletContext servletContext) {
	//CONTEXT_CLASS_PARAM 在初始化的时候一般都不会赋值，所以contextClassName=null
    String contextClassName = servletContext.getInitParameter(CONTEXT_CLASS_PARAM);
    if (contextClassName != null) {
        try {
            return ClassUtils.forName(contextClassName, ClassUtils.getDefaultClassLoader());
        }
        catch (ClassNotFoundException ex) {
            throw new ApplicationContextException(
                    "Failed to load custom context class [" + contextClassName + "]", ex);
        }
    }
    else {
    	//defaultStrategies在类初始化的时候会被赋值
        contextClassName = defaultStrategies.getProperty(WebApplicationContext.class.getName());
        try {
            return ClassUtils.forName(contextClassName, ContextLoader.class.getClassLoader());
        }
        catch (ClassNotFoundException ex) {
            throw new ApplicationContextException(
                    "Failed to load default context class [" + contextClassName + "]", ex);
        }
    }
}
```

如上所示：defaultStrategies在类初始化的时候会被赋值,该值在ContextLoader.properties中。最终发现context的值为XmlWebApplicationContext,回到上面的initWebApplicationContext()方法中，context创建完成后configureAndRefreshWebApplicationContext()会被执行，并来到最终wac.refresh();

```
@Override
public void refresh() throws BeansException, IllegalStateException {
    synchronized (this.startupShutdownMonitor) {
        prepareRefresh();
        //创建beanFactory，跟进去发现最终创建的为DefaultListableBeanFactory，跟上面使用的一致
        ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();
        prepareBeanFactory(beanFactory);
        try {
        	//下面就是webApplicationContext初始化的一些操作
            postProcessBeanFactory(beanFactory);
            invokeBeanFactoryPostProcessors(beanFactory);
            registerBeanPostProcessors(beanFactory);
            initMessageSource();
            initApplicationEventMulticaster();
            onRefresh();
            registerListeners();
            finishBeanFactoryInitialization(beanFactory);
            finishRefresh();
        }
        catch (BeansException ex) {
			、、、
        }
        finally {
            resetCommonCaches();
        }
    }
}
```

创建beanFactory后会执行XmlWebApplicationContext的loadBeanDefinitions()方法，该方法会加载root.xml和application.xml。都会委托BeanDefinitionParser去执行，包括<context:component-scan/>、<mvc:annotation-driven/>、<task:annotation-driven/>、<interceptors/>等等。

## 结语

到这里关于bean加载的四种注解解析告一段落了，但是装载这些bean后，如何使用呢？@Autowired是如何注入的？@RequestMapping是如何映射请求的？@ModelAttribute等是如何工作的？
@Async异步请求如何处理的？等等。  后续会继续更新~




1. 获取配置bean：**Component**,**Controller**,**Repository**,**Service**
2. 获取request信息相关注解： **PathVariable**,**RequestParam**,**RequestBody**,**RequestHeader**,**CookieValue**,**ModelAttribute**,**SessionAttributes**
3. 校验表单参数相关注解：**Valid**,**AssertTrue/AssertFalse**,**DecimalMax/DecimalMin**,**Digits**,**Future/Past**,**Max/Min**,**NotNull/Null**,**Pattern**,**Size**
4. aop相关注解：**不建议使用**，建议在配置文件中配置，便于维护
5. 异步注解：**Async**

