Ext.define('alexzam.his.view.account.TransactionGrid', {
    extend:'Ext.grid.Panel',
    alias:'widget.his.account.TransactionGrid',

    requires:[
        'alexzam.his.model.account.store.Transaction',
        'alexzam.his.model.account.proxy.Transaction',
        'Ext.grid.column.Date',
        'Ext.grid.column.Number',
        'Ext.grid.feature.Summary'
    ],

    multiSelect:true,

    columns:[
        {
            header:'Когда',
            dataIndex:'timestamp',
            xtype:'datecolumn',
            format:'d.m.Y'
        },
        {
            header:'Кто',
            dataIndex:'actor_name'
        },
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
            flex:1,
            autoScroll:true,
            renderer:function(val) {
                return "<span title='" + val + "'>" + val + "</span>";
            }
        }
    ],

    selType:'rowmodel',

    features:[
        {ftype:'summary'}
    ],

    viewConfig:{
        getRowClass:function (record) {
            var t = record.get('type');
            if (t.length == 1) return "acc-transrow-" + t;
            else return '';
        }
    },

    proxyTrans: null,

    initComponent:function () {
        var me = this;

        me.proxyTrans = Ext.create('alexzam.his.model.account.proxy.Transaction', {
            rootUrl:me.rootUrl
        });

        me.store = Ext.create('alexzam.his.model.account.store.Transaction', {
            proxy:me.proxyTrans
        });

        me.callParent();

        me.getView().getFeature(0).disable();
    },

    reloadTrans:function(data) {
        var me = this;
        me.proxyTrans.extraParams = data;
        me.store.load();
    },

    enableSummary:function(enable){
        var feat = this.getView().getFeature(0);
        if(enable) feat.enable();
        else feat.disable();
    }
});