var transStore;
var catStore;

var account = {
    loadTransactions:function() {
        transStore.close();

        var frm = dijit.byId('frmFilter');
        if (!frm.validate()) return;

        var q = frm.getValues();
        if (q.from != null) {
            q.from = q.from.getTime();
        }
        if (q.to != null) {
            q.to = q.to.getTime();
        }

        transStore.url = transStoreUrl + '?' + dojo.objectToQuery(q);
        transStore.fetch({
            onComplete:function() {
                // When transaction store is updated
                dijit.byId('tabTrans').setStore(transStore);
            }
        });
    },

    loadCategories:function(firstTime) {
        catStore.close();
        catStore.url = catStoreUrl;
        catStore.fetch({
            onComplete:function() {
                var cbCat = dijit.byId('cbCategory');
                cbCat.store = catStore;
                cbCat.query = {"type":'e'};
                var selFilterCat = dijit.byId('filter_category');
                if (firstTime) {
                    selFilterCat.setStore(catStore, 0);
                    account.loadTransactions();
                } else {
                    selFilterCat.setStore(catStore);
                }
            }
        });
    },

    addSubmit:function() {
        var frm = dijit.byId('frmAddTrans');
        if (!frm.validate()) return;

        var data = frm.getValues();
        data.date = data.date.getTime();

        var cbcat = dijit.byId('cbCategory');
        if (cbcat.item == null) {
            // New category
            data.catname = data.cat;
            data.cat = 0;
        } else {
            data.cat = cbcat.item.id[0];
        }

        if (data.actor == 0) data.actor = uid;
        data.act = 'put';

        dojo.xhrPost({
            content:data,
            url:transStoreUrl,
            load:function() {
                account.loadTransactions();
                account.loadCategories(false);
                account.updateAccountStats();

                dijit.byId('taAddComment').set('value', '');
                dijit.byId('cbCategory').set('value', '');
                dijit.byId('tbAddAmount').set('value', '');
            }
        });
    },

    updateAccountStats:function() {
        dojo.xhrGet({
            url:transStoreUrl + '?act=getamount',
            handleAs:'json',
            load:function(data) {
                dojo.byId('account_amount').innerHTML = data.amount + ' р.';
                dojo.byId('valTotalExp').innerHTML = data.totalExp;
                dojo.byId('valEachExp').innerHTML = data.eachExp;
                dojo.byId('valPersExp').innerHTML = data.persExp;
                dojo.byId('valPersDonation').innerHTML = data.persDonation;
                dojo.byId('valPersSpent').innerHTML = data.persSpent;
                dojo.byId('valPersBalance').innerHTML = data.persBalance;
            }
        });
    },

    onTableColResize:function() {
    },

    onFilterChange:function() {
        account.loadTransactions();
    },

    onAddTypeChange:function() {
        var type = dijit.byId('frmAddTrans').getValues().type;
        dijit.byId('cbCategory').set('disabled', (type == 'i' || type == 'r'));
    },

    onBtDelete: function() {
        var tab = dijit.byId('tabTrans');
        var selItems = tab.selection.selected;
        var delIds = new Array();

        for (var i = 0; i < selItems.length; i++) {
            if (selItems[i] != true) continue;
            delIds.push(tab.getItem(i).id[0]);
        }

        if (delIds.length <= 0) return;
        var idsStr = delIds.join();

        var data = {act:"del", ids:idsStr};
        dojo.xhrPost({
            content:data,
            url:transStoreUrl,
            load:function() {
                account.loadTransactions();
                account.updateAccountStats();
            }
        });
    }
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
var srcStoreCats = {
    model: 'Category',
    storeId: 'stCats',
    proxy: {
        type: 'ajax',
        url: catStoreUrl,
        reader: {
            type: 'json',
            root: 'items'
        }
    },
    autoLoad: true,
    filters:[
        {
            property: 'type',
            value: 'e'
        }
    ]
};

var srcAddForm = {
    xtype: 'form',
    title: 'Добавить транзакцию',
    layout: 'hbox',
    bodyPadding: 5,
    items:[
        {
            xtype: 'radiogroup',
            fieldLabel: 'Кто',
            labelAlign: 'top',
            vertical: true,
            columns: 1,
            minWidth: 90,
            items: userRadioOptions
        },
        {
            xtype: 'datefield',
            fieldLabel: 'Когда',
            name: 'date',
            maxValue: new Date(),
            format: 'd.m.Y',
            validateOnChange: false
        },
        {
            xtype: 'panel',
            layout: 'vbox',
            height: 80,
            border: 0,
            bodyPadding: '0 0 0 5px',
            items:[
                {
                    xtype: 'numberfield',
                    name: 'amount',
                    fieldLabel: 'Сколько',
                    minValue: 0.01,
                    hideTrigger: true,
                    keyNavEnabled: false,
                    mouseWheelEnabled: false
                },
                {
                    xtype: 'radiogroup',
                    width: 300,
                    layout: 'vbox',
                    height: 50,
                    items: [
                        {
                            xtype: 'panel',
                            layout: 'hbox',
                            border: 0,
                            width: 300,
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
                            width: 300,
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
            bodyPadding: '0 0 0 5px',
            items:[
                {
                    xtype: 'combo',
                    fieldLabel: 'Категория',
                    queryMode: 'local',
                    store: 'stCats',
                    valueField: 'id',
                    displayField: 'name',
                    lastQuery: ''
                },
                {
                    xtype: 'textfield',
                    fieldLabel: 'Комментарий',
                    name: 'comment'
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

var srcScreen = {
    layout: 'border',
    items: [
        {
            xtype: 'panel',
            region: 'north',
            height: 120,
            layout: 'border',
            items:[
                {
                    xtype: 'panel',
                    region: 'east',
                    width: 100
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
            width: 150,
            split: true
        },
        {
            xtype: 'panel',
            region: 'center'
        }
    ]
};

var proxyCats;

Ext.onReady(function() {
    Ext.define('Category', {
        extend: 'Ext.data.Model',
        fields: [
            {name: 'id', type: 'String'},
            {name: 'name', type: 'String'},
            {name: 'type', type: 'String'}
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

    Ext.create('Ext.container.Viewport', srcScreen);
});