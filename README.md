> ## [開始使用 RQC][RQC] ##


Q & A
=====

RQC 是什麼？
------------

RQC 是 Reference、Quote、Citation 的縮寫，
是一個處理「參考資料引用」的輔助軟體。

RQC 與常見軟體不同，它沒有資料庫、也沒有資料輸入介面。
所有資料都是在 Google Sheet 上輸入與儲存。

RQC 提供（相對於 Google Sheet）便利的閱讀、檢索介面，
以及寫作時各種格式的引用文字產生器。


類似軟體很多，為什麼需要 RQC？
------------------------------

因為[需求發起者][Alice]的 reference 都是古老書籍，
許多軟體主打針對電子期刊的功能變得毫無意義。

RQC 不需要安裝、沒有備份問題，
對於原本就在使用 Google Sheet 的人而言幾乎沒有學習成本，
這是撰寫 RQC 的出發點。

喔對，RQC 還完全免費。


完全免費？
----------

是的，唯二需要的東西就是網路跟 Google 帳號。

~~當然如果你願意打賞開發人員，我們也非常樂於接受...... [伸手]~~


使用設定
========

基礎設定方式
------------

1. 需要有 Google / G-Suite 帳號，並在瀏覽器中登入
1. 開啟 [RQC 空白範本][RQC sheet]試算表
1. 在畫面上方選單列，依序點選「檔案」→「建立副本」，
	按下「確定」後會開啟新的瀏覽器視窗。


以下步驟在新的瀏覽器視窗（新建立的試算表副本）內操作：

1. 按畫面上方「共用」按鈕
	+ 跳出的對話框中，按下方「取得連結」
	+ 確認中間權限為「知道連結的使用者」
1. 複製瀏覽器網址


以下步驟 **建議** 在另外的瀏覽器視窗內操作：

1. 開啟 [RQC]
1. 點選「系統設定」頁籤
1. 右方畫面按下「新增」
	1. 下方「試算表網址」輸入框中貼上剛剛複製的網址
	1. 勾選「預設開啟」
	1. 按下確定
1. 重新載入網頁。


進階設定方式及原理
------------------

RQC 預設開啟的試算表是依下列優先順序決定：

0. 網址 query string 指定（key 值：`sheetId`）
1. 網頁的 JS 程式碼指定（變數：`sheetId`）
2. 瀏覽器的 storage 有設定值，且當中有指定預設開啟的試算表
3. RQC 預設的 demo 試算表

因此，若有同時讀取多個試算表的需求、
又或是不希望使用者都得自行設定才能顯示指定試算表資料，
可自行儲存 [RQC] 網頁、修改 JS 程式碼（含 Google API key）後放到網頁空間，
或是儲存在本機硬碟中以 `file:///d:/foo/rqc.html` 的方式開啟亦可。
請自行類推。


注意事項
========

* 試算表的共用設定必須為「知道連結的使用者」（檢視者即可）
* 試算表內的工作表名稱 **不能** 變動（順序可以）
* 工作表的第一列（row），各行（column）的值不能改變，也就是欄位名稱不能改變。
	* 但是欄位先後順序可調，也就是允許第一行與第三行調換。
* 若想自行 build RQC，由於 dependency 的 [GST] 與 [GF] 尚未進 Maven Repository，
	請先作 [GST] / [GF] 的 `mvn install`。


狀況排除
========

* 為什麼會顯示「系統不接受此瀏覽器」？
	* RQC 用了一些 ~~先進的~~ 瀏覽器功能，這導致某些瀏覽器無法正常使用。
		~~請趕快拋棄 IE，~~ 建議改用 Chrome 或 FireFox。
* 為什麼會顯示「讀取資料錯誤」？
	1. 若初次設定，請檢查 Google Sheet 網址是否複製錯誤；
	2. 若之前都能正常使用，請檢查試算表是否（依舊）存在、
		再檢查「發布到網路」上的設定是否正常。
	3. 也有極為渺小的機率是 Google Sheet 服務本身就當機...... :scream:
* 換了一個瀏覽器或是換一台電腦，為什麼開啟時就看不到自己設定的資料？
	* 是的，**這是正常現象**。
		RQC 的設定值是儲存在瀏覽器提供的空間。
		當換一個瀏覽器或是換一台電腦時，自然就讀取不到設定。
		所以得重新進行一次「基礎設定方式」的步驟。
	* 也可參考前述「進階設定方式及原理」的章節，
		以 JS 程式碼指定的方式，
		則無論哪一台電腦、哪一個瀏覽器開啟該網頁，預設都會開啟同一個試算表。


[Alice]: https://alice.psmonkey.org/
[RQC sheet]: https://docs.google.com/spreadsheets/d/1P2ch_lO9ncI7QGl3O1RDwk3XCkbxc9n_8GAnZCAgo4k
[RQC]: https://dontcareabout.github.io/CDN/RQC.html
[GF]: https://github.com/DontCareAbout/GF
[GST]: https://github.com/DontCareAbout/GoogleSheetToolkit
