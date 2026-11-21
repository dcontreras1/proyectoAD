use bienestar_en_casa;

db.reservas.aggregate([
  {
    $group: {
      _id: "$estado",
      totalReservas: { $sum: 1 },
      montoTotalSnapshot: { $sum: "$servicioSnapshot.precio" },
      duracionPromedioMinutos: { $avg: "$servicioSnapshot.duracionMinutos" }
    }
  },
  {
    $sort: { totalReservas: -1 }
  }
]);

db.reservas.aggregate([
  {
    $match: {
      estado: { $in: ["ACEPTADA", "COMPLETADA"] }
    }
  },
  {
    $group: {
      _id: "$proveedorId",
      serviciosConfirmadosOCompletados: { $sum: 1 },
      ingresosGenerados: { $sum: "$servicioSnapshot.precio" }
    }
  },
  {
    $lookup: {
      from: "usuarios",
      localField: "_id",
      foreignField: "_id",
      as: "proveedor"
    }
  },
  {
    $unwind: "$proveedor"
  },
  {
    $project: {
      _id: 0,
      proveedorId: "$_id",
      nombreProveedor: "$proveedor.nombre",
      tituloProfesional: "$proveedor.perfilProfesional.titulo",
      serviciosConfirmadosOCompletados: 1,
      ingresosGenerados: 1
    }
  },
  {
    $sort: { ingresosGenerados: -1 }
  }
]);

db.reservas.aggregate([
  {
    $group: {
      _id: "$clienteId",
      totalSolicitudes: { $sum: 1 },
      completadas: {
        $sum: { $cond: [{ $eq: ["$estado", "COMPLETADA"] }, 1, 0] }
      },
      canceladas: {
        $sum: { $cond: [{ $eq: ["$estado", "CANCELADA"] }, 1, 0] }
      },
      rechazadas: {
        $sum: { $cond: [{ $eq: ["$estado", "RECHAZADA"] }, 1, 0] }
      }
    }
  },
  {
    $lookup: {
      from: "usuarios",
      localField: "_id",
      foreignField: "_id",
      as: "cliente"
    }
  },
  {
    $unwind: "$cliente"
  },
  {
    $project: {
      _id: 0,
      clienteId: "$_id",
      nombreCliente: "$cliente.nombre",
      totalSolicitudes: 1,
      completadas: 1,
      canceladas: 1,
      rechazadas: 1
    }
  }
]);

db.reservas.aggregate([
  {
    $match: {
      estado: { $in: ["RECHAZADA", "CANCELADA"] },
      motivo: { $ne: null }
    }
  },
  {
    $group: {
      _id: {
        estado: "$estado",
        motivo: "$motivo"
      },
      cantidad: { $sum: 1 }
    }
  },
  {
    $sort: { cantidad: -1 }
  }
]);

db.reservas.aggregate([
  {
    $group: {
      _id: "$servicioSnapshot.nombre",
      vecesSolicitado: { $sum: 1 },
      precioPromedioSnapshot: { $avg: "$servicioSnapshot.precio" },
      facturacionTotal: { $sum: "$servicioSnapshot.precio" }
    }
  },
  {
    $sort: { vecesSolicitado: -1 }
  }
]);