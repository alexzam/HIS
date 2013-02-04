Ext.define('alexzam.his.view.account.TopPanel', {
    extend:'Ext.panel.Panel',
    alias:'widget.his.account.TopPanel',

    requires:[
        'Ext.layout.container.Border',
        'alexzam.his.view.account.AddTransactionForm'
    ],

    layout:'border',

    items:[
        {
            xtype:'panel',
            region:'east',
            width:230,
            layout:'fit',
            itemId:'pnlAmount',
            html:'<span id="account_amount"></span>'
        }
    ],

    spanAmount:null,
    frmTrans:null,

    initComponent:function () {
        var me = this;

        me.items.push(Ext.create('Ext.panel.Panel', {
            region:'center',
            layout:'fit',
            items:[
                Ext.create('alexzam.his.view.account.AddTransactionForm', {
                    userRadioOptions:me.userRadioOptions,
                    rootUrl:me.rootUrl,
                    uid:me.uid,
                    itemId:'frmTrans',
                    bubbleEvents:['transchanged']
                })
            ]
        }));

        me.callParent();
        me.frmTrans = me.getComponent(1).getComponent('frmTrans');
    },

    setAccAmount:function(val) {
        this.spanAmount.innerHTML = val + '&nbsp;р.';
    },

    afterRender:function() {
        var me = this;
        me.callParent(arguments);

        me.spanAmount = me.getComponent('pnlAmount').getEl().getById('account_amount').dom;
    },

    reloadCats:function() {
        this.frmTrans.reloadCats();
    }
});