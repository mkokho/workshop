package nz.co.airnz.docker.api

import com.amazonaws.services.ecs.model.Cluster
import com.amazonaws.services.ecs.model.Service

data class DashboardData(
    val clusters: List<Cluster>,
    val services: List<Service>
)