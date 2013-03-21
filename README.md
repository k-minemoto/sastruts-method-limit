sastruts-method-limit
==============
SAStrutsのActionクラスの実行メソッドを呼び出す前に、HTTPメソッドによる呼び出し制限チェックを提供します。

Apache等の上位レイヤーではなく、webアプリケーションで制御を行う場合に使用できます。  
バリデーションよりも先にチェックされます。

インストール
------
mavenから取得できます。pomファイルのdependenciesに次の用に追加してください。

    <dependency>
        <groupId>net.jp.saf.sastruts</groupId>
        <artifactId>sastruts-method-limit</artifactId>
        <version>0.1.0</version>
    </dependency>

初期設定
------
### 1. sastruts-actionfilterの設定

[sastruts-actionfilter](https://github.com/k-minemoto/sastruts-actionfilter "sastruts-actionfilter")を使用しているため、こちらを先にセットアップして下さい。

### 2. チェックコンポーネントの登録

app.dicon、またはapp.diconからincludeされているdiconファイルに次のコンポーネント定義を行って下さい。

#### app.dicon、またはapp.diconからincludeされているdiconファイル

    <component name="httpMethodChecker" class="net.jp.saf.sastruts.method.impl.HttpMethodCheckerImpl"/>

### 3. Actionfilterの登録

SMART deployによる登録を行う場合、convention.diconにルートパッケージの追加を行って下さい。

#### convention.dicon

    <component class="org.seasar.framework.convention.impl.NamingConventionImpl">
        ～それぞれのアプリ設定～
        <initMethod name="addRootPackageName">
            <arg>"net.jp.saf.sastruts.method.smartdeploy"</arg>
        </initMethod>
    </component>

SMART deployを使用しない場合、個別にコンポーネント登録を行って下さい。

#### httpMethodCheckerをコンポーネント定義したdiconファイル

    <component name="httpMethodLimitActionfilter" class="net.jp.saf.sastruts.method.smartdeploy.actionfilter.HttpMethodLimitActionfilter"/>
    </component>

### 4. FilterContainerへの追加

早い段階でチェックするべきだと考えられるので、FilterContainerの先頭に追加して下さい。

#### FilterContainerをコンポーネント定義したdiconファイル

    <component class="net.jp.saf.sastruts.actionfilter.FilterContainer">
        <initMethod name="addActionfilter"><arg>"httpMethodLimitActionfilter"</arg></initMethod>
        ～その他の定義～
    </component>


使い方
------
Actionクラスの実行メソッドにアノテーションを設定することで、チェックが行われます。

サンプルActionクラス

    public static class FooAction {

        @Execute(validator=false)
        public String index() {
            return "index.jsp";
        }

        @HttpMethodLimit
        @Execute(validator=false)
        public String method1() {
            return "method1.jsp";
        }

        @HttpMethodLimit({HttpMethod.POST})
        @Execute(validator=false)
        public String method2() {
            return "method2.jsp";
        }

        @HttpMethodLimit(extensionHeader=ExtensionHeaderBehavior.HEADER_ONLY)
        @Execute(validator=false)
        public String method3() {
            return "method3.jsp";
        }

        @HttpMethodLimit(value={HttpMethod.GET}, checkForward=true)
        @Execute(validator=false)
        public String onlyGet() {
            return "onlyGet.jsp";
        }

        @HttpMethodLimit({HttpMethod.POST})
        @Execute(validator=false)
        public String method4() {
            return "onlyGet";
        }
    }

### HttpMethodLimitアノテーション無しの実行メソッド

FooActionのindex()は、HttpMethodLimitアノテーションが付いていません。  
この場合、デフォルトではチェックを行いません。

この挙動を変更する場合、httpMethodCheckerのコンポーネント定義を変更することで対応できます。

    <component name="httpMethodChecker" class="net.jp.saf.sastruts.method.impl.HttpMethodCheckerImpl">
        <property name="defaultAllows">
        {
            @net.jp.saf.sastruts.method.enums.HttpMethod@GET
        }
        </property>
    </component>

defaultAllowsは、アノテーションが付いて無い場合にチェックするHTTPメソッドをList形式で設定します。  
Listに設定できるのは、net.jp.saf.sastruts.method.enums.HttpMethod に定義された列挙型のみです。  
上記のように設定した場合、HttpMethodLimitアノテーション無しの実行メソッドはGETリクエストのみ許可されることになります。

なお、net.jp.saf.sastruts.method.enums.HttpMethod には、次のものが定義されています。

__標準的なHTTPメソッド__
* GET
* POST
* PUT
* DELETE
* HEAD
* OPTIONS
* TRACE

__上記の組み合わせ__
* GET_POST  
    GETとPOST。
* REST_CRUD  
    GET、POST、PUT、DELETEの4つ。
* ALL  
    標準HTTPメソッド全部

### HttpMethodLimitアノテーションありの実行メソッド

FooActionのmethod1()は、HttpMethodLimitアノテーションが付いてます。  
その他属性を設定していないため、次のデフォルト設定でチェックを行います。

* HTTPメソッドはGET、またはPOSTのどちらか
* 拡張ヘッダーは使用しない
* forwardで呼びだされた時はチェックしない

この挙動を変更する場合、httpMethodCheckerのコンポーネント定義を変更することで対応できます。

    <component name="httpMethodChecker" class="net.jp.saf.sastruts.method.impl.HttpMethodCheckerImpl">
        <property name="defaultAnnotationAllows">
        {
            @net.jp.saf.sastruts.method.enums.HttpMethod@POST,
            @net.jp.saf.sastruts.method.enums.HttpMethod@GET,
            @net.jp.saf.sastruts.method.enums.HttpMethod@PUT
        }
        </property>
        <property name="extensionHeaderName">"X-Http-Method-Override"</property>
        <property name="extensionHeader">@net.jp.saf.sastruts.method.enums.ExtensionHeaderBehavior@NOT_USE</property>
    </component>

__value省略時の設定__

value省略時の設定は、defaultAnnotationAllowsにチェックするHTTPメソッドをList形式で設定します。  
Listに設定できるのは、net.jp.saf.sastruts.method.enums.HttpMethod に定義された列挙型のみです。  
上記のように設定した場合、value省略時の実行メソッドはGET、POST、PUTリクエストが許可されることになります。

__拡張ヘッダーの設定__

実際のリクエストはPOSTで行われるが、チェックはPUTとしてチェックする必要がある場合、拡張ヘッダーを使用することが可能です。  
拡張ヘッダー名は、デフォルトで「X-Http-Method-Override」が定義されています。  
これを変更する場合、extensionHeaderNameに値を設定して下さい。

また、デフォルトでは拡張ヘッダーは使用しないようになっています。  
この挙動を変更するには、extensionHeaderに net.jp.saf.sastruts.method.enums.ExtensionHeaderBehavior の列挙型を設定して下さい。

なお、ExtensionHeaderBehaviorには次のものが定義されています。

* NOT_USE  
    拡張ヘッダーを使用しない
* HEADER_FIRST  
    拡張ヘッダーがリクエストに存在すれば使用し、なければHTTPリクエストメソッドを使用する
* HEADER_ONLY  
    拡張ヘッダーの値のみを使用する。HTTPリクエストメソッドは無視する
* NOT_DEFINE  
    NOT_USEと同じ扱い。HttpMethodLimitアノテーションで指定した場合、httpMethodCheckerの設定に従うことになる。

__forward呼び出し時のチェック__

このデフォルト定義を変更することはできません。

### HttpMethodLimitアノテーションで実行メソッド指定

FooActionのmethod2()はHttpMethodLimitアノテーションが付いてあり、さらにHttpMethod.POSTが設定されています。  
この場合、デフォルトチェックの内容から使用できるHTTPメソッドを上書きします。

よって、method2は次のようになります。
* HTTPメソッドはPOSTのみ
* 拡張ヘッダーは使用しない
* forwardで呼びだされた時はチェックしない

### HttpMethodLimitアノテーションで拡張ヘッダー使用の指定

FooActionのmethod3()はHttpMethodLimitアノテーションが付いてあり、さらにextensionHeaderにExtensionHeaderBehavior.HEADER_ONLYが設定されています。  
この場合、デフォルトチェックの内容から拡張ヘッダーの使用を上書きします。

よって、method3は次のようになります。
* HTTPメソッドはGET、またはPOSTのどちらか
* 拡張ヘッダーがあれば使用する
* forwardで呼びだされた時はチェックしない

### HttpMethodLimitアノテーションでforward呼び出し時もチェックする

FooActionのonlyGet()はHttpMethodLimitアノテーションが付いてあり、さらにcheckForwardにtrueが設定されています。  
この場合、FooActionのmethod4のように別アクション等からforwardで呼びだされた場合にも、onlyGet()は実行メソッドをチェックします。

上記のmethod4とonlyGetのように、設定がかみ合って無いと常にエラー判定されるので注意して下さい。


### チェックエラー時の挙動を変更する

デフォルトでは、チェックエラーの場合は405エラーを返すように設定しています。  
この挙動を変更する場合、httpMethodCheckerのコンポーネント定義を変更することで対応できます。

    <component name="httpMethodChecker" class="net.jp.saf.sastruts.method.impl.HttpMethodCheckerImpl">
        <property name="errorHandler">
          <component class="net.jp.saf.sastruts.method.helpers.impl.MethodNotAllowedErrorHandler"/>
        </property>
    </component>

errorHandlerには、インタフェース net.jp.saf.sastruts.method.helpers.ErrorHandler の実装クラスを設定して下さい。

標準の実装として、次の3つを提供しています。

#### net.jp.saf.sastruts.method.helpers.impl.MethodNotAllowedErrorHandler

405エラー(Method Not Allowed)を返します。  
コンポーネントは、次のように定義できます。

    <component class="net.jp.saf.sastruts.method.helpers.impl.MethodNotAllowedErrorHandler">
        <property name="disableAllowHeader">false</property>
        <property name="forward">""</property>
    </component>

各プロパティは、次の内容です。

__disableAllowHeader__

405エラーを返す場合、RFC2616では使用できるHTTPメソッドをAllowヘッダーで返すことになっています。  
trueにすることで、Allowヘッダーを送らないようにします。  
デフォルトはfalseです。

__forward__

405エラー時のフォワード先がある場合、ここにパスを設定します。  
フォワード先が未設定の場合、エラー時にはHttpServletResponse.sendErrorが呼ばれることになります。
デフォルトは未設定です。


#### net.jp.saf.sastruts.method.helpers.impl.NotImplementedErrorHandler

501エラー(Not Implemented)を返します。  
コンポーネントは、次のように定義できます。

    <component class="net.jp.saf.sastruts.method.helpers.impl.NotImplementedErrorHandler">
        <property name="forward">""</property>
    </component>

ここで設定できるforwardは、MethodNotAllowedErrorHandlerと同じです。

#### net.jp.saf.sastruts.method.helpers.impl.ThrowExceptionErrorHandler

エラーレスポンスを発生させず、代わりに net.jp.saf.sastruts.method.exception.MethodNotAllowedRuntimeException を発生させます。

MethodNotAllowedRuntimeExceptionを適切に処理して下さい。

