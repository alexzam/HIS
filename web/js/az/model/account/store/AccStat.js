Ext.define('alexzam.his.model.account.store.AccStat', {
    extend:'Ext.data.Store',
    requires:[
        'alexzam.his.model.account.AccStat'
    ],

    model:'alexzam.his.model.account.AccStat',
    data:[
        {id:'TE', name:'����� �������', val:'0'},
        {id:'EE', name:'�� �������', val:'0'},
        {id:'PE', name:'��� �����', val:'0'},
        {id:'PD', name:'��� ����� � �����', val:'0'},
        {id:'PS', name:'����� ���� ��������', val:'0'},
        {id:'PB', name:'��� ������', val:'0'}
    ]
});