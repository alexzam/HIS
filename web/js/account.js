var account = {
    loadTransactions:function() {
        var frm = Ext.getCmp('frmFilter');
        if (!frm.getForm().isValid()) return;

        var q = frm.getForm().getFieldValues();
        if (q.from != null) {
            q.from = q.from.getTime();
        }
        if (q.to != null) {
            q.to = q.to.getTime();
        }

        proxyTrans.extraParams = q;

        Ext.data.StoreManager.getByKey('stTrans').load();
    },

    loadCategories:function() {
        var cmp = Ext.getCmp('cbCategory');
        var val = cmp.getValue();
        cmp.setValue('');
        Ext.data.StoreManager.getByKey('stCats').load();
        cmp.setValue(val);
        cmp.getPicker().setLoading(false);

        cmp = Ext.getCmp('cbFilterCategory');
        val = cmp.getValue();
        cmp.setValue('');
        Ext.data.StoreManager.getByKey('stFilterCats').load();
        cmp.setValue(val);
        cmp.getPicker().setLoading(false);
    },

    setAddFormFullValidation:function(enable) {
        var cmp = Ext.getCmp('tbAddDate');
        cmp.allowBlank = !enable;
        cmp.isValid();
        cmp = Ext.getCmp('tbAddAmount');
        cmp.allowBlank = !enable;
        cmp.isValid();
        cmp = Ext.getCmp('cbCategory');
        cmp.allowBlank = !enable;
        cmp.isValid();
    },

    resetAddForm:function() {
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

        if (!tbAddDate.isValid() || !tbAddAmount.isValid() || !cbCat.isValid()) {
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
                account.loadCategories();
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
                var store = Ext.data.StoreManager.getByKey('stAccStats');
                store.getById('TE').set('val', data.totalExp);
                store.getById('EE').set('val', data.eachExp);
                store.getById('PE').set('val', data.persExp);
                store.getById('PD').set('val', data.persDonation);
                store.getById('PS').set('val', data.persSpent);
                store.getById('PB').set('val', data.persBalance);
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
    },

    onBtDelete: function() {
        var selected = Ext.getCmp('gridTrans').getSelectionModel().getSelection();
        var delIds = [];

        for (i in selected) {
            var sel = selected[i];
            delIds.push(sel.get('id'));
        }

        if (delIds.length <= 0) return;
        var idsStr = delIds.join();

        var data = {act:"del", ids:idsStr};
        Ext.Ajax.request({
            method:'POST',
            url:transStoreUrl,
            params:data,
            callback:function() {
                account.loadTransactions();
                account.loadCategories();
                account.updateAccountStats();
            }
        });
    }
};

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
            width: 175,
            listeners:{
                change: account.onFilterChange
            }
        },
        {
            xtype: 'button',
            text: 'Удалить',
            handler: account.onBtDelete
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
                srcFilterForm,
                {
                    xtype:'grid',
                    region:'south',
                    store:'stAccStats',
                    columns:[
                        {header:'Чего', dataIndex:'name', sortable:false, menuDisabled: true},
                        {header:'Сколько', dataIndex:'val', flex:1, sortable:false, menuDisabled: true}
                    ]
                }
            ]
        },
        {
            xtype: 'grid',
            region: 'center',
            store:'stTrans',
            id: 'gridTrans',
            multiSelect: true,
            columns:[
                {
                    header:'Когда',
                    dataIndex:'timestamp',
                    xtype:'datecolumn',
                    format:'d.m.Y'
                },
                {header:'Кто', dataIndex:'actor_name'},
                {
                    header:'Сколько',
                    dataIndex:'amount',
                    xtype:'numbercolumn',
                    summaryType:'sum'
                },
                {
                    header:'Категория',
                    dataIndex:'category_name'
                },
                {
                    header:'Комментарий',
                    dataIndex:'comment',
                    flex: 1
                }
            ],
            selType: 'rowmodel',
            features: [
                {ftype: 'summary'}
            ],
            viewConfig: {
                getRowClass: function(record) {
                    var t = record.get('type');
                    if (t.length == 1) return "acc-transrow-" + t;
                    else return '';
                }
            }
        }
    ]
};

Ext.define('Category', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'id', type: 'string'},
        {name: 'name', type: 'string'},
        {name: 'type', type: 'string'}
    ]
});

Ext.define('Transaction', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'id', type: 'string'},
        {name: 'actor_id', type: 'int'},
        {name: 'actor_name', type: 'string'},
        {name: 'amount', type: 'float'},
        {name: 'category_name', type: 'string'},
        {name: 'comment', type: 'string'},
        {name: 'type', type: 'string'},
        {name: 'timestamp', type: 'date', dateFormat: 'd.m.Y'}
    ]
});

Ext.define('AccStat', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'id', type: 'string'},
        {name: 'name', type: 'string'},
        {name: 'val', type: 'string'}
    ]
});

var proxyCats;
var proxyAddCats;
var proxyTrans;

Ext.onReady(function() {
    Ext.util.Format.thousandSeparator = ' ';

    proxyCats = new Ext.data.proxy.Ajax({
        url: catStoreUrl,
        model: 'Category',
        reader: {
            type: 'json',
            root: 'items'
        }
    });

    proxyAddCats = new Ext.data.proxy.Ajax({
        url: catStoreUrl,
        model: 'Category',
        extraParams: {type: 'e'},
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
        proxy: proxyAddCats,
        autoLoad: true
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

    Ext.create('Ext.data.Store', {
        model: 'AccStat',
        storeId: 'stAccStats',
        data : [
            {id: 'TE', name: 'Общие расходы', val: '0'},
            {id: 'EE', name: 'На каждого', val: '0'},
            {id: 'PE', name: 'Мои траты', val: '0'},
            {id: 'PD', name: 'Мой вклад в Казну', val: '0'},
            {id: 'PS', name: 'Всего моих расходов', val: '0'},
            {id: 'PB', name: 'Мой баланс', val: '0'}
        ]
    });

    Ext.create('Ext.container.Viewport', srcScreen);

    var d = new Date();
    d.setDate(1);
    Ext.getCmp('tbFilterFrom').setValue(d);

    account.updateAccountStats();
});