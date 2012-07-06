Ext.define('alexzam.his.AccountScreen', {
    extend:'Ext.container.Viewport',

    requires:[
        'alexzam.his.view.account.TopPanel',
        'alexzam.his.view.account.RightPanel',
        'alexzam.his.view.account.TransactionGrid',
        'Ext.Ajax'
    ],

    layout:'border',

    rightPanel:null,
    topPanel:null,
    grdTrans:null,
    storeStats:null,

    tbar: [
        { xtype: 'button', text: 'Button 1' }
    ],

    initComponent:function () {
        var me = this;

        me.storeStats = Ext.create('alexzam.his.model.account.store.AccStat', {});

        me.items = [
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
                listeners:{
                    filterupdate:me.onTransReload,
                    transdelete:me.onBtTransDelete,
                    scope:me
                }
            },
            {
                xtype:'his.account.TransactionGrid',
                region:'center',
                rootUrl:me.rootUrl,
                itemId:'grdTrans'
            }
        ];

        me.callParent();

        me.rightPanel = me.getComponent('panelR');
        me.topPanel = me.getComponent('panelT');
        me.grdTrans = me.getComponent('grdTrans');

        me.reloadAccStats();
    },

    onTransReload:function() {
        var me = this;
        var q = me.rightPanel.getFilterData();

        if (q == null) return;

        if (q.from != null) {
            q.from = q.from.getTime();
        }
        if (q.to != null) {
            q.to = q.to.getTime();
        }
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
