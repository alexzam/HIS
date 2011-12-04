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

    initComponent:function () {
        var me = this;

        var storeStats = Ext.create('alexzam.his.model.account.store.AccStat', {
            storeId:'stAccStats'
        });

        me.frmFilter = Ext.create('alexzam.his.view.account.FilterForm', {
            region:'center',
            rootUrl:me.rootUrl
        });

        me.grdStats = Ext.create('Ext.grid.Panel', {
            region:'south',
            store:storeStats,
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

    getFilterData:function(){
        var frm = this.frmFilter.getForm();
        if (!frm.isValid()) return null;

        return frm.getFieldValues();
    }
});