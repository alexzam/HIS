Ext.define('alexzam.his.AccountScreen', {
    extend:'Ext.container.Viewport',

    requires:[
        'alexzam.his.view.account.TopPanel',
        'alexzam.his.view.account.RightPanel',
        'alexzam.his.view.account.TransactionGrid'
    ],

    layout:'border',

    rightPanel:null,
    grdTrans:null,

    initComponent:function ()
    {
        var me = this;

        me.items = [
            {
                xtype:'his.account.TopPanel',
                rootUrl:me.rootUrl,
                userRadioOptions:me.userRadioOptions,
                uid:me.uid,
                region:'north',
                height:125,
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
                itemId: 'panelR'
            },
            {
                xtype:'his.account.TransactionGrid',
                region:'center',
                rootUrl:me.rootUrl,
                itemId:'grdTrans'
            }
        ];

        me.callParent();

        me.rightPanel=me.getComponent('panelR');
        me.grdTrans=me.getComponent('grdTrans');
    },

    onTransactionsChanged:function(){
        var me = this;
        var q = me.rightPanel.getFilterData();

        if(q == null) return;

        if (q.from != null) {
            q.from = q.from.getTime();
        }
        if (q.to != null) {
            q.to = q.to.getTime();
        }
        me.grdTrans.reloadTrans(q);
    }
});
