var account = {
    loadTransactions:function() {
        Ext.data.StoreManager.getByKey('stTrans').load();
//        transStore.close();
//
//        var frm = dijit.byId('frmFilter');
//        if (!frm.validate()) return;
//
//        var q = frm.getValues();
//        if (q.from != null) {
//            q.from = q.from.getTime();
//        }
//        if (q.to != null) {
//            q.to = q.to.getTime();
//        }
//
//        transStore.url = transStoreUrl + '?' + dojo.objectToQuery(q);
//        transStore.fetch({
//            onComplete:function() {
//                // When transaction store is updated
//                dijit.byId('tabTrans').setStore(transStore);
//            }
//        });
    },

//    loadCategories:function(firstTime) {
//        catStore.close();
//        catStore.url = catStoreUrl;
//        catStore.fetch({
//            onComplete:function() {
//                var cbCat = dijit.byId('cbCategory');
//                cbCat.store = catStore;
//                cbCat.query = {"type":'e'};
//                var selFilterCat = dijit.byId('filter_category');
//                if (firstTime) {
//                    selFilterCat.setStore(catStore, 0);
//                    account.loadTransactions();
//                } else {
//                    selFilterCat.setStore(catStore);
//                }
//            }
//        });
//    },

    setAddFormFullValidation:function(enable){
        var cmp = Ext.getCmp('tbAddDate');
        cmp.allowBlank = !enable;
        cmp.isValid();
        cmp = Ext.getCmp('tbAddAmount');
        cmp.allowBlank = !enable;
        cmp.isValid();
        cmp = Ext.getCmp('cmp.isValid();');
        cmp.allowBlank = !enable;
        cmp.isValid();
    },

    resetAddForm:function(){
        Ext.getCmp('tbAddAmount').setValue(null);
        Ext.getCmp('cbCategory').setValue(null);
    },

    addSubmit:function() {
        var frm = Ext.getCmp('frmAddTrans');
        if (!frm.getForm().isValid()) return;

        var data = frm.getValues();
        var tbAddDate = Ext.getCmp('tbAddDate');
        var tbAddAmount = Ext.getCmp('tbAddAmount');
        var cbCat = Ext.getCmp('cbCategory');

        account.setAddFormFullValidation(true);

        if(!tbAddDate.isValid() || !tbAddAmount.isValid() || !cbCat.isValid()){
            Ext.MessageBox.show({title:'Еггог',msg:'Инвалид!',icon:Ext.MessageBox.ERROR});
            return;
        }

        data.date = tbAddDate.getValue().getTime();
        if (cbCat.getRawValue() == data.cat) {
            // New category
            data.catname = data.cat;
            data.cat = 0;
        }

        if (data.actor == 0) data.actor = uid;
        data.act = 'put';

        Ext.Ajax.request({
            url: transStoreUrl,
            params: data,
            success:function() {
                account.loadTransactions();
//                account.loadCategories(false);
                account.updateAccountStats();
            }
        });

        account.resetAddForm();
        account.setAddFormFullValidation(false);
    },

    logout: function() {
        document.location = "login?mode=out";
    },

    updateAccountStats:function() {
        Ext.Ajax.request({
            url:transStoreUrl + '?act=getamount',
            callback:function(o, s, resp) {
                var data = Ext.JSON.decode(resp.responseText);
                Ext.get('account_amount').dom.innerHTML = data.amount + '&nbsp;р.';
//                dojo.byId('valTotalExp').innerHTML = data.totalExp;
//                dojo.byId('valEachExp').innerHTML = data.eachExp;
//                dojo.byId('valPersExp').innerHTML = data.persExp;
//                dojo.byId('valPersDonation').innerHTML = data.persDonation;
//                dojo.byId('valPersSpent').innerHTML = data.persSpent;
//                dojo.byId('valPersBalance').innerHTML = data.persBalance;
            }
        });
    },

    onFilterChange:function() {
        account.loadTransactions();

        var tbFrom = Ext.getCmp('tbFilterFrom');
        var tbTo = Ext.getCmp('tbFilterTo');
        tbTo.setMinValue(tbFrom.getValue());
        tbFrom.setMaxValue(tbTo.getValue());
    },

    onAddTypeChange:function(me, val) {
        var type = val.type;
        Ext.getCmp('cbCategory').setDisabled(type == 'i' || type == 'r');
    }
//
//    onBtDelete: function() {
//        var tab = dijit.byId('tabTrans');
//        var selItems = tab.selection.selected;
//        var delIds = new Array();
//
//        for (var i = 0; i < selItems.length; i++) {
//            if (selItems[i] != true) continue;
//            delIds.push(tab.getItem(i).id[0]);
//        }
//
//        if (delIds.length <= 0) return;
//        var idsStr = delIds.join();
//
//        var data = {act:"del", ids:idsStr};
//        dojo.xhrPost({
//            content:data,
//            url:transStoreUrl,
//            load:function() {
//                account.loadTransactions();
//                account.updateAccountStats();
//            }
//        });
//    }
};

//dojo.addOnLoad(function() {
//    transStore = new dojo.data.ItemFileWriteStore({
//        url: transStoreUrl,
//        clearOnClose: true,
//        urlPreventCache: true
//    });
//
//    catStore = new dojo.data.ItemFileReadStore({
//        clearOnClose: true,
//        urlPreventCache: true
//    });
//
//    account.loadCategories(true);
//
//    dijit.byId('cbCategory').query = {"type":'e'};
//
//    dijit.byId('rbTypeExpPers').onChange = account.onAddTypeChange;
//    dijit.byId('rbTypeExpAcc').onChange = account.onAddTypeChange;
//    dijit.byId('rbTypeInc').onChange = account.onAddTypeChange;
//    dijit.byId('rbTypeRef').onChange = account.onAddTypeChange;
//
//    var from = new Date();
//    from.setDate(1);
//    dijit.byId('filter_datefrom').set('value', from);
//
//    account.updateAccountStats();
//
//    dijit.byId('btDelete').onClick = account.onBtDelete;
//});

var srcAddForm = {
    xtype: 'form',
    id: 'frmAddTrans',
    title: 'Добавить транзакцию',
    layout: 'hbox',
    bodyPadding: 5,
    autoScroll: true,
    items:[
        {
            xtype: 'radiogroup',
            fieldLabel: 'Кто',
            labelAlign: 'top',
            vertical: true,
            columns: 1,
            minWidth: 100,
            allowBlank: false,
            items: userRadioOptions
        },
        {
            xtype: 'datefield',
            fieldLabel: 'Когда',
            id: 'tbAddDate',
            name: 'date',
            maxValue: new Date(),
            format: 'd.m.Y',
            validateOnChange: false,
            allowBlank: true,
            labelWidth: 40
        },
        {
            xtype: 'panel',
            layout: 'vbox',
            height: 80,
            border: 0,
            bodyPadding: '0 0 0 5px',
            items:[
                {
                    xtype: 'panel',
                    layout: 'hbox',
                    width: 250,
                    border: 0,
                    items:[
                        {
                            xtype: 'numberfield',
                            name: 'amount',
                            fieldLabel: 'Сколько',
                            minValue: 0.01,
                            hideTrigger: true,
                            keyNavEnabled: false,
                            mouseWheelEnabled: false,
                            labelWidth: 50,
                            validateOnChange: false,
                            width: 155,
                            labelAlign: 'left',
                            allowBlank: true,
                            id: 'tbAddAmount'
                        },
                        {
                            xtype: 'panel',
                            border: 0,
                            html: '<span class="labCurrency">RUR</span>'
                        }
                    ]
                },
                {
                    xtype: 'radiogroup',
                    width: 250,
                    layout: 'vbox',
                    height: 50,
                    allowBlank: false,
                    listeners:{
                        change: account.onAddTypeChange
                    },
                    items: [
                        {
                            xtype: 'panel',
                            layout: 'hbox',
                            border: 0,
                            width: 250,
                            items: [
                                {
                                    xtype: 'radio',
                                    boxLabel: 'Трата из своих',
                                    name: 'type',
                                    inputValue: 'p',
                                    margin: '0 5 0 0'
                                },
                                {
                                    xtype: 'radio',
                                    boxLabel: 'Трата из Казны',
                                    name: 'type',
                                    inputValue: 'a'
                                }
                            ]
                        },
                        {
                            xtype: 'panel',
                            layout: 'hbox',
                            border: 0,
                            width: 250,
                            items: [
                                {
                                    xtype: 'radio',
                                    boxLabel: 'Вклад в Казну',
                                    name: 'type',
                                    inputValue: 'i',
                                    margin: '0 5 0 0'
                                },
                                {
                                    xtype: 'radio',
                                    boxLabel: 'Возмещение из Казны',
                                    name: 'type',
                                    inputValue: 'r'
                                }
                            ]
                        }
                    ]
                }
            ]
        },
        {
            xtype: 'panel',
            layout: 'vbox',
            height: 80,
            border: 0,
            bodyPadding: '0 5px 0 5px',
            items:[
                {
                    xtype: 'combo',
                    fieldLabel: 'Категория',
                    name: 'cat',
                    id: 'cbCategory',
                    queryMode: 'local',
                    store: 'stCats',
                    valueField: 'id',
                    displayField: 'name',
                    lastQuery: '',
                    labelWidth: 80,
                    allowBlank: true
                },
                {
                    xtype: 'textfield',
                    fieldLabel: 'Комментарий',
                    name: 'comment',
                    labelWidth: 80
                }
            ]
        },
        {
            xtype: 'button',
            text: 'Добавить',
            handler: account.addSubmit
        }
    ]
};

var srcFilterForm = {
    xtype: 'form',
    id: 'frmFilter',
    region:'center',
    title: 'Фильтр',
    layout: 'vbox',
    bodyPadding: 5,
    autoScroll: true,
    items:[
        {
            xtype: 'datefield',
            fieldLabel: 'С',
            id: 'tbFilterFrom',
            name: 'from',
            format: 'd.m.Y',
            validateOnChange: false,
            labelWidth: 65,
            width: 175,
            listeners:{
                change: account.onFilterChange
            }
        },
        {
            xtype: 'datefield',
            fieldLabel: 'По',
            id: 'tbFilterTo',
            name: 'to',
            format: 'd.m.Y',
            validateOnChange: false,
            labelWidth: 65,
            width: 175,
            listeners:{
                change: account.onFilterChange
            }
        },
        {
            xtype: 'combo',
            fieldLabel: 'Категория',
            name: 'cat',
            id: 'cbFilterCategory',
            queryMode: 'local',
            store: 'stFilterCats',
            valueField: 'id',
            displayField: 'name',
            lastQuery: '',
            labelWidth: 65,
            width: 175
        }
    ]
};

var srcScreen = {
    layout: 'border',
    items: [
        {
            xtype: 'panel',
            region: 'north',
            height: 125,
            layout: 'border',
            items:[
                {
                    xtype: 'panel',
                    region: 'east',
                    width: 230,
                    layout: 'border',
                    items:[
                        {
                            xtype: 'button',
                            region:'north',
                            text: 'Выйти',
                            handler: account.logout
                        },
                        {
                            xtype:'panel',
                            region:'center',
                            layout:'fit',
                            html:'<span id="account_amount">load</span>'
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    region:'center',
                    layout: 'fit',
                    items:[
                        srcAddForm
                    ]
                }
            ]
        },
        {
            xtype: 'panel',
            region: 'east',
            width: 200,
            layout:'border',
            items:[
                srcFilterForm
            ]
        },
        {
            xtype: 'grid',
            region: 'center',
            store:'stTrans',
            columns:[
                {header:'Когда', dataIndex:'timestamp'},
                {header:'Кто', dataIndex:'actor_name'},
                {header:'Сколько', dataIndex:'amount'},
                {header:'Категория', dataIndex:'category_name'},
                {header:'Комментарий', dataIndex:'comment', flex: 1}
            ]
        }
    ]
};

var proxyCats;
var proxyTrans;

Ext.onReady(function() {
    Ext.define('Category', {
        extend: 'Ext.data.Model',
        fields: [
            {name: 'id', type: 'String'},
            {name: 'name', type: 'String'},
            {name: 'type', type: 'String'}
        ]
    });
    Ext.define('Transaction', {
        extend: 'Ext.data.Model',
        fields: [
            {name: 'id', type: 'String'},
            {name: 'actor_id', type: 'int'},
            {name: 'actor_name', type: 'String'},
            {name: 'amount', type: 'String'},
            {name: 'category_name', type: 'String'},
            {name: 'comment', type: 'String'},
            {name: 'timestamp', type: 'String'}
        ]
    });
    proxyCats = new Ext.data.proxy.Ajax({
        url: catStoreUrl,
        model: 'Category',
        reader: {
            type: 'json',
            root: 'items'
        }
    });

    proxyTrans = new Ext.data.proxy.Ajax({
        url: transStoreUrl,
        model: 'Transaction',
        reader: {
            type: 'json',
            root: 'items'
        }
    });

    new Ext.data.Store({
        model: 'Category',
        storeId: 'stCats',
        proxy: proxyCats,
        autoLoad: true,
        filters:[
            {
                property: 'type',
                value: 'e'
            }
        ]
    });
    new Ext.data.Store({
        model: 'Category',
        storeId: 'stFilterCats',
        proxy: proxyCats,
        autoLoad: true
    });
    new Ext.data.Store({
        model: 'Transaction',
        storeId: 'stTrans',
        proxy: proxyTrans,
        autoLoad: true
    });

    Ext.create('Ext.container.Viewport', srcScreen);

    account.updateAccountStats();
});