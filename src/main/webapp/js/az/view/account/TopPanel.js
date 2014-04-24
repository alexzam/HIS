Ext.define('alexzam.his.view.account.TopPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.his.account.TopPanel',

    /*
     * TopPanel (Panel)
     *   az-accChooser "accChooser" (Panel)
     *      label
     *      combo "cbAccount"
     *   panel "pnlAmount"
     *   AddTransactionForm "frmTrans"
     */

    requires: [
        'Ext.layout.container.Border',

        'alexzam.his.view.account.AccountChooser',
        'alexzam.his.view.account.AddTransactionForm'
    ],

    layout: 'border',

    items: [
        {
            xtype: 'az-accChooser',
            itemId: 'accChooser',
            region: 'north',
            bubbleEvents: ['filterupdate']
        },
        {
            xtype: 'panel',
            region: 'east',
            width: 230,
            layout: 'fit',
            itemId: 'pnlAmount',
            html: '<span id="account_amount"></span>'
        },
        {
            xtype: 'his.account.AddTransactionForm',
            region: 'center',
            itemId: 'frmTrans',
            bubbleEvents: ['transchanged']
        }
    ],

    spanAmount: null,
    frmTrans: null,

    initComponent: function () {
        var me = this;

        me.callParent();
        me.frmTrans = me.getComponent('frmTrans');
    },

    setAccAmount: function (val) {
        this.spanAmount.innerHTML = val + '&nbsp;р.';
    },

    afterRender: function () {
        var me = this;
        me.callParent(arguments);

        me.spanAmount = me.getComponent('pnlAmount').getEl().getById('account_amount').dom;
    },

    reloadCats: function () {
        this.frmTrans.reloadCats();
    },

    getFilterData: function () {
        return {aid: this.getComponent("accChooser").getValue()};
    }
});