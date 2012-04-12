Ext.define('alexzam.his.view.account.RightPanel', {
    extend:'Ext.panel.Panel',
    alias:'widget.his.account.RightPanel',

    requires:[
        'Ext.layout.container.Border',
        'alexzam.his.model.account.store.Category',
        'alexzam.his.model.account.proxy.Category',
        'alexzam.his.model.account.store.AccStat',
        'alexzam.his.view.account.FilterForm'
    ],

    layout:'border',

    frmFilter:null,
    grdStats:null,
    storeStats:null,

    initComponent:function () {
        var me = this;

        me.frmFilter = Ext.create('alexzam.his.view.account.FilterForm', {
            region:'center',
            rootUrl:me.rootUrl,
            bubbleEvents:['filterupdate', 'transdelete']
        });

        me.grdStats = Ext.create('Ext.grid.Panel', {
            region:'south',
            store:me.storeStats,
            columns:[
                {header:'Чего', dataIndex:'name', sortable:false, menuDisabled:true},
                {header:'Сколько', dataIndex:'val', flex:1, sortable:false, menuDisabled:true}
            ]
        });

        me.items = [
            me.frmFilter,
            me.grdStats
        ];

        me.callParent();
    },

    getFilterData:function() {
        var frm = this.frmFilter.getForm();
        if (!frm.isValid()) return null;

        return frm.getFieldValues();
    },

    reloadCategories:function() {
        this.frmFilter.reloadCategories();
    }
});