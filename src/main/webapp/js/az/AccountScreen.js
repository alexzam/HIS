Ext.define('alexzam.his.AccountScreen', {
    extend:'Ext.container.Viewport',

    requires:[
        'alexzam.his.view.account.TopPanel',
        'alexzam.his.view.account.RightPanel',
        'alexzam.his.view.account.TransactionGrid',
        'alexzam.his.view.toolbar.Toolbar',
        'Ext.Ajax'
    ],

    layout:'fit',

    rightPanel:null,
    topPanel:null,
    grdTrans:null,
    storeStats:null,

    items:[
        {
            xtype:'panel',
            layout:'border',
            itemId:'pnlOuter',
            dockedItems:[{
                xtype:'his.Toolbar',
                dock:'top',
                disableId:'acc'
            }]
        }
    ],


    initComponent:function () {
        var me = this;

        me.storeStats = Ext.create('alexzam.his.model.account.store.AccStat', {});

        me.items[0].items = [
            {
                xtype:'his.account.TopPanel',
                rootUrl:me.rootUrl,
                userRadioOptions:me.userRadioOptions,
                uid:me.uid,
                region:'north',
                height:100,
                itemId:'panelT',
                listeners:{
                    transchanged:me.onTransactionsChanged,
                    scope:me
                }
            },
            {
                xtype:'his.account.RightPanel',
                rootUrl:me.rootUrl,
                region:'east',
                width:200,
                itemId: 'panelR',
                storeStats:me.storeStats,
            },
            {
                xtype:'his.account.TransactionGrid',
                region:'center',
                rootUrl:me.rootUrl,
                itemId:'grdTrans'
            }
        ];

        me.callParent();

        var pnlOuter = me.getComponent('pnlOuter');
        me.rightPanel = pnlOuter.getComponent('panelR');
        me.topPanel = pnlOuter.getComponent('panelT');
        me.grdTrans = pnlOuter.getComponent('grdTrans');

        me.on({
            filterupdate: me.onTransReload,
            transdelete: me.onBtTransDelete,
            scope: me
        });

        me.reloadAccStats();
    },

    onTransReload:function() {
        var me = this;
        var q = me.rightPanel.getFilterData();

        if (q == null) return;

        me.grdTrans.reloadTrans(q);

        me.grdTrans.enableSummary(q.cat > 0);
    },

    onTransactionsChanged:function() {
        var me = this;

        me.onTransReload();
        me.rightPanel.reloadCategories();
        me.reloadAccStats();
    },

    reloadAccStats:function() {
        var me = this;
        Ext.Ajax.request({
            url:me.rootUrl + 'account/stats',
            callback:function(o, s, resp) {
                var data = Ext.JSON.decode(resp.responseText);
                me.topPanel.setAccAmount(data.amount);
                var store = me.storeStats;
                store.getById('TE').set('val', data.totalExp);
                store.getById('EE').set('val', data.eachExp);
                store.getById('PE').set('val', data.persExp);
                store.getById('PD').set('val', data.persDonation);
                store.getById('PS').set('val', data.persSpent);
                store.getById('PB').set('val', data.persBalance);
            }
        });
    },

    onBtTransDelete: function() {
        var me = this;
        var selected = me.grdTrans.getSelectionModel().getSelection();
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
            url:me.rootUrl + 'account/data',
            params:data,
            callback:function() {
                me.onTransactionsChanged();
                me.topPanel.reloadCats();
            }
        });
    }
});
